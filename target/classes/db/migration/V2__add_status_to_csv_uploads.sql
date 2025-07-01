ALTER TABLE csv_uploads ADD COLUMN status VARCHAR(50);

UPDATE csv_uploads SET status = 'Completed'