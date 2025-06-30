CREATE TABLE csv_export (
    id UUID PRIMARY KEY,
    upload_id UUID NOT NULL,
    progress INT DEFAULT 0,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    completed_at TIMESTAMP,
    file_path TEXT,
    error_message TEXT,

    CONSTRAINT fk_upload FOREIGN KEY (upload_id)
        REFERENCES csv_uploads(id)
        ON DELETE CASCADE
);
