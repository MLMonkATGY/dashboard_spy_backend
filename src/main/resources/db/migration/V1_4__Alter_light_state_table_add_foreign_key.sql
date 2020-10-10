ALTER TABLE room_light_states
ADD CONSTRAINT fk_device_states
FOREIGN KEY (device_id)
REFERENCES devices_info_table
(device_id) ON DELETE CASCADE;
