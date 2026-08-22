import pandas as pd
from pandas import DataFrame
from os.path import join


# data found on https://davidmegginson.github.io/ourairports-data/airports.csv
def cleanData(input_file):
    dtypes = {
        "type": "string",
        "name": "string",
        "continent": "string",
        "iso_country": "string",
        "municipality": "string",
        "scheduled_service": "string",
        "icao_code": "string",
        "gps_code": "string",
        "wikipedia_link": "string",
    }
    df = pd.read_csv(
        join("scripts", "rawData", input_file),
        delimiter=",",
        encoding="utf8",
        dtype=dtypes,
        usecols=[
            "type",
            "name",
            "latitude_deg",
            "longitude_deg",
            "continent",
            "iso_country",
            "municipality",
            "scheduled_service",
            "icao_code",
            "gps_code",
            "wikipedia_link",
        ],
    )

    df = df.rename(
        columns={
            "iso_country": "country",
            "latitude_deg": "latitude",
            "longitude_deg": "longitude",
        }
    )

    df["code"] = df["icao_code"].fillna(df["gps_code"]).str.upper()
    df["latitude"] = pd.to_numeric(df["latitude"], errors="coerce")
    df["longitude"] = pd.to_numeric(df["longitude"], errors="coerce")
    df["scheduled_service"] = (
        df["scheduled_service"].fillna("no").map({"yes": True, "no": False})
    )
    df["type"] = df["type"].map(
        {"small_airport": "Small", "medium_airport": "Medium", "large_airport": "Large"}
    )

    df = df.drop_duplicates(subset="code", keep="first")

    mask = (
        df["type"].isin(["Small", "Medium", "Large"])
        & df["scheduled_service"]
        & df["continent"].isin(["AF", "AN", "AS", "EU", "NA", "OC", "SA"])
        & df["latitude"].between(-90, 90)
        & df["longitude"].between(-180, 180)
    )

    filtered_df = df[mask].dropna(subset=["code", "name", "country"]).copy()

    filtered_df = filtered_df[
        [
            "code",
            "name",
            "continent",
            "country",
            "municipality",
            "wikipedia_link",
            "latitude",
            "longitude",
            "type",
        ]
    ]

    print(
        f"{len(df)} rows read -> {len(filtered_df)} valid rows after filtering "
        f"({len(df) - len(filtered_df)} dropped)"
    )
    return filtered_df


def sql_literal(value):
    """Convert a single Python value into a safe SQL literal string."""
    if pd.isna(value):
        return "NULL"
    if isinstance(value, bool):
        return "TRUE" if value else "FALSE"
    if isinstance(value, (int, float)):
        return str(value)

    escaped = str(value).replace("'", "''")
    return f"'{escaped}'"


def transformToSQL(df: DataFrame, output_file):
    columns_sql = ", ".join(df.columns)
    row_strings = []

    for _, row in df.iterrows():
        values_sql = ", ".join(sql_literal(v) for v in row)
        row_strings.append(f"   ({values_sql})")

    with open(join("scripts", output_file), "w", encoding="utf-8") as f:
        f.write(f"INSERT INTO airport ({columns_sql}) VALUES\n")
        f.write(",\n".join(row_strings))
        f.write(";\n")


# put the raw data inside /scripts/rawData/
data = cleanData("airports.csv")
transformToSQL(data, "V3__seed_airports.sql")
