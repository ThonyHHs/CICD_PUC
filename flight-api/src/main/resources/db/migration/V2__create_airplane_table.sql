CREATE TABLE airplane (
    code    VARCHAR(5)  PRIMARY KEY,
    name    VARCHAR(20) NOT NULL,
    range   INTEGER     NOT NULL CHECK (range > 0),
    speed   INTEGER     NOT NULL CHECK (speed > 0),
    type    VARCHAR(10) NOT NULL,

    CONSTRAINT chk_airplane_type CHECK (
        type IN ('Small', 'Medium', 'Large')
    )
);
