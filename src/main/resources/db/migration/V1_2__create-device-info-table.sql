CREATE TABLE devices_info_table (
	device_id  VARCHAR(100) PRIMARY KEY,
	device_name VARCHAR(100) not NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	local_address VARCHAR(100) NOT NULL UNIQUE,
	api_key VARCHAR(100) NOT NULL UNIQUE,
	initialization_vector_on VARCHAR(100) NOT NULL,
	encrypted_payload_on VARCHAR(100) NOT NULL ,
	initialization_vector_off VARCHAR(100) NOT NULL ,
	encrypted_payload_off VARCHAR(100) NOT NULL ,
	self_api VARCHAR(50) NOT NULL,
	switch_state BOOLEAN NOT NULL DEFAULT FALSE

);