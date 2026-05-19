-- Test Data for Real Estate Portal

-- Insert test users using MERGE so it won't fail or overwrite if they already exist
MERGE INTO users (id, first_name, last_name, email, password, phone_number, address, city, state, zip_code, is_active, created_at, updated_at) 
KEY(id)
VALUES (1, 'John', 'Doe', 'john@example.com', 'password123', '555-0101', '123 Main St', 'New York', 'NY', '10001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO users (id, first_name, last_name, email, password, phone_number, address, city, state, zip_code, is_active, created_at, updated_at) 
KEY(id)
VALUES (2, 'Jane', 'Smith', 'jane@example.com', 'password456', '555-0102', '456 Oak Ave', 'Los Angeles', 'CA', '90001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO users (id, first_name, last_name, email, password, phone_number, address, city, state, zip_code, is_active, created_at, updated_at) 
KEY(id)
VALUES (3, 'Robert', 'Johnson', 'robert@example.com', 'password789', '555-0103', '789 Pine Rd', 'Chicago', 'IL', '60601', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
