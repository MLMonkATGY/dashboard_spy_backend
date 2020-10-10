CREATE TABLE room_light_states (
	id serial PRIMARY KEY,
	seq INTEGER not NULL,
	created_at TIMESTAMP NOT NULL Default NOW(),
	light_state BOOLEAN NOT NULL

	
);