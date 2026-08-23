# Formativa_CICD

Uma API REST feita usando Spring Boot para geração de rotas aleatórias para uso em simuladores, usando bases de dados reais de [OurAirports](https://davidmegginson.github.io/ourairports-data/airports.csv) e [SimBrief](https://www.simbrief.com/api/inputs.airframes.json).

## Tecnologias usadas

- Java 25 + Spring Boot
- PostgreSQL 16
- Docker + Docker Compose
- Python - (Usado nos scripts de limpeza de dados e geração dos arquivos SQL)

## Getting Started

### Pré-requisitos

- Docker e Docker Compose
- Opcional - Java 25 e Maven (Para execução local sem Docker)

### Docker Compose

```bash
git clone https://github.com/ThonyHHs/CICD_PUC.git
cd CICD_PUC
docker compose up --build
```

Serão iniciados dois contêineres:

- PostgreSQL, que será populado automaticamente com os scripts em `db/`
- API, que estará disponível em `http://localhost:8080`

### Localmente com a imagem do Docker

```bash
docker pull thonyhhs/flight-api:latest
docker run --env-file .env -p 8080:8080 thonyhhs/flight-api:latest
```

Use o arquivo `.env.example` para a configuração da conexão com um banco de dados PostgreSQL.

### Localmente sem Docker

```bash
git clone https://github.com/ThonyHHs/CICD_PUC.git
cd CICD_PUC/flight-api
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Altere o arquivo `./src/main/resources/application-dev.yml` para a conexão com o banco de dados.

## API Endpoints

### GET - `/api/resource/airplanes`
Lista todos os aviões

```json
[
  {
    "code": "A20N",
    "name": "A320-200N"
  }
]
```

### GET - `/api/resource/continents`
Lista todos os continentes

```json
[
  "AF",
  "AN",
  "AS",
  "EU",
  "NA",
  "OC",
  "SA"
]
```

### GET - `/api/route?airplaneCode={code}&continent={continent}`
Lista todos os aviões

```json
{
    "departure": {
        "code": "SBSL",
        "name": "Marechal Cunha Machado International Airport",
        "continent": "SA",
        "country": "BR",
        "municipality": "São Luís",
        "wikipediaLink": "https://en.wikipedia.org/wiki/Marechal_Cunha_Machado_International_Airport"
    },
    "arrival": {
        "code": "SBCX",
        "name": "Hugo Cantergiani Regional Airport",
        "continent": "SA",
        "country": "BR",
        "municipality": "Caxias Do Sul",
        "wikipediaLink": "https://en.wikipedia.org/wiki/Caxias_do_Sul_Airport"
    },
    "airplane": {
        "code": "A20N",
        "name": "A320-200N"
    },
    "flightTime": "PT3H44M",
    "distanceNm": 1646,
    "simbriefLink": "https://dispatch.simbrief.com/options/custom?type=A20N&orig=SBSL&dest=SBCX"
}
```
## Geração dos scripts SQL
Os arquivos em `db/` são gerados pelos dois scripts em `scripts/`.

Para a geração desses arquivos:
```bash
cd scripts
mkdir rawData
```

Faça o download dos seguintes arquivos e copie-os para a pasta `rawData/`:
- `airports.csv` - [OurAirports](https://davidmegginson.github.io/ourairports-data/airports.csv)
- `inputs.airframes.json` - [SimBrief](https://www.simbrief.com/api/inputs.airframes.json)

Após isso, execute os scripts:
```bash 
pip install  -r requirements.txt
python clean_airports.py
python clean_airplanes.py
```
Os arquivos serão gerados dentro da pasta `scripts/`.

Após os arquivos serem gerados, copie-os para a pasta `db/` na raíz do projeto.


## License 
Veja [LICENSE](./LICENSE).
