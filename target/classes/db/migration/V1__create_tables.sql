CREATE TABLE csv_uploads (
    id UUID PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    numberRecords INT NOT NULL,
    uploadData TIMESTAMP NOT NULL
);

CREATE TABLE person (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    middlename VARCHAR(100),
    email VARCHAR(150),
    gender VARCHAR(50),
    ip_acess VARCHAR(50),
    age INT,
    approximate_date_of_birth DATE,
    csv_upload_id UUID REFERENCES csv_uploads(id)
);
