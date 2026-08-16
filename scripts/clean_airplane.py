from os.path import join
from math import sqrt
import re
import json

RESERVE_FACTOR = 0.8


# data found on https://www.simbrief.com/api/inputs.airframes.json
def getSpeed(speedStr: str, ceilingAltitude: int) -> int:
    cruiseAltitude = ceilingAltitude
    if re.search("Mach", speedStr):
        mach = float(speedStr.split(" ")[-1])
        oat = 15 - (2 * (cruiseAltitude // 1000))
        lss = 39 * sqrt(273 + oat)
        tas = mach * lss
        return int(tas)
    elif re.search("KIAS", speedStr):
        ias = int(speedStr.split(" ")[0])
        tas = ias * (1 + (cruiseAltitude // 1000) * 0.02)
        return int(speedStr.split(" ")[0])
    else:
        return -1


def getRange(maxFuelLbs, fuelFlowLbs, speed):
    usable_fuel = maxFuelLbs * RESERVE_FACTOR
    endurance_hours = usable_fuel / fuelFlowLbs
    range_nm = speed * endurance_hours
    return int(range_nm)


def getType(type: str) -> str:
    match type:
        case "L":
            return "Small"
        case "M":
            return "Medium"
        case "H":
            return "Large"
        case "J":
            return "Large"
        case _:
            return "Unknown"


def filterAirplanes(inputPath) -> list:
    with open(join("scripts", "rawData", inputPath), "r", encoding="utf-8") as f:
        data = json.load(f)

    airplane_list = []

    for _, value in data.items():
        airframe_options = value["airframes"][0]["airframe_options"]
        maxFuelLbs = airframe_options["maxfuel"]
        fuelFlowLbs = value["aircraft_fuelflow_lbs"]
        speed = getSpeed(value["aircraft_speed"], value["aircraft_ceiling"])
        airplane = {
            "code": value["aircraft_icao"],
            "name": value["aircraft_name"],
            "range": getRange(maxFuelLbs, fuelFlowLbs, speed),
            "speed": speed,
            "type": getType(airframe_options["cat"]),
        }

        if (
            airplane["type"] == "Unknown"
            or speed < 0
            or airplane["code"] == "ZZZZ"
            or len(airplane["code"]) > 5
        ):
            continue

        airplane_list.append(airplane)

    print(f"{len(airplane_list)} airplanes generated")
    for a in sorted(airplane_list, key=lambda x: x["range"])[:5]:
        print(f"  lowest range: {a}")
    for a in sorted(airplane_list, key=lambda x: x["range"], reverse=True)[:5]:
        print(f"  highest range: {a}")
    return airplane_list


def sql_literal(value):
    """Convert a single Python value into a safe SQL literal string."""
    if value is None:
        return "NULL"
    if isinstance(value, bool):
        return "TRUE" if value else "FALSE"
    if isinstance(value, (int, float)):
        return str(value)

    escaped = str(value).replace("'", "''")
    return f"'{escaped}'"


def transformToSQL(data: list[dict], output_file):
    columns_sql = ", ".join(data[0].keys())
    row_strings = []

    for airplane in data:
        sql_string = ", ".join(sql_literal(v) for v in airplane.values())
        row_strings.append(f"   ({sql_string})")

    with open(join("scripts", output_file), "w", encoding="utf-8") as f:
        f.write(f"INSERT INTO airplane ({columns_sql}) VALUES\n")
        f.write(",\n".join(row_strings))
        f.write(";\n")


data = filterAirplanes("inputs.airframes.json")
transformToSQL(data, "V4__seed_airplanes.sql")
