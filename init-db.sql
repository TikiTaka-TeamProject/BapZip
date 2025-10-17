CREATE USER testuser WITH PASSWORD 'testpassword';

CREATE DATABASE testdb WITH OWNER testuser;

\c testdb

CREATE EXTENSION IF NOT EXISTS postgis;

GRANT ALL PRIVILEGES ON SCHEMA public TO testuser;