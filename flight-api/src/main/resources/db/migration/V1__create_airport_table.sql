CREATE TABLE airport (
    code            VARCHAR(4)   PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    continent       VARCHAR(2)   NOT NULL,
    country         VARCHAR(2)   NOT NULL,
    municipality    VARCHAR(100),
    wikipedia_link  VARCHAR(255),
    latitude        DOUBLE PRECISION NOT NULL,
    longitude       DOUBLE PRECISION NOT NULL,
    type            VARCHAR(10)  NOT NULL,

    CONSTRAINT chk_continent CHECK (
        continent IN ('AF', 'AN', 'AS', 'EU', 'NA', 'OC', 'SA')
    ),
    CONSTRAINT chk_airport_type CHECK (
        type IN ('Small', 'Medium', 'Large')
    ),
    CONSTRAINT chk_latitude CHECK (latitude BETWEEN -90 AND 90),
    CONSTRAINT chk_longitude CHECK (longitude BETWEEN -180 AND 180)
);

CREATE INDEX idx_airport_continent ON airport (continent);