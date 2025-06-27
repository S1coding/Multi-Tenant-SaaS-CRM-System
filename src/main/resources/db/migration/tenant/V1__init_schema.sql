CREATE TABLE tenants (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255),
    password VARCHAR(255),
    company VARCHAR(255),
    phone_number VARCHAR(20),
    authority VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE contacts (
    id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255),
    phone_number VARCHAR(20),
    company VARCHAR(255),
    position VARCHAR(100),
    notes TEXT,
    tenant VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE deals (
    id VARCHAR(255) PRIMARY KEY,
    made_by VARCHAR(255),
    made_to VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE reports (
    id VARCHAR(255) PRIMARY KEY,
    made_by VARCHAR(255),
    for_project VARCHAR(255),
    title VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE tasks (
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255),
    task_project VARCHAR(255),
    assigned_to VARCHAR(255),
    notes TEXT,
    due_date TIMESTAMP,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
