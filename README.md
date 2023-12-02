# employer-worker-registration-system
An accounting program that contains employee and employer information and records of relationships between them.

---

# v1 branch
## Screenshot
<p align="center"><strong>Login</strong></p>
<p align="center"><img src="https://user-images.githubusercontent.com/71611710/157845415-c8f293df-5e1a-4ac5-a066-1971ee3ab6ae.png"></p>

| **Homepage**            | **Employer registration**|  **Worker registration**
:------------------------:|:------------------------:|:-------------------------:
![2-home_page](https://user-images.githubusercontent.com/71611710/157845986-0b99502d-ec6a-411c-999c-d37859dcf47e.png) | ![3-new_employer](https://user-images.githubusercontent.com/71611710/157849241-2a4ea23f-f195-4152-ab57-b2da20a1ea87.png)  |  ![3-new_worker](https://user-images.githubusercontent.com/71611710/157849850-5c6cfda1-05cd-4164-8287-474496cd189e.png)

| **Search Box**  | **Registration document**
:----------------:|:-------------------------:
![5-view_worker](https://user-images.githubusercontent.com/71611710/157850829-c03944a1-bd1b-41d6-875b-61f8d8ce4d62.png) | ![7-new_record_optionpane](https://user-images.githubusercontent.com/71611710/158039292-30c103d1-bdaa-4f3f-bd36-342815fd6efd.png)

---

## Requirements
Postgresql is used in this program. You can find the necessary jar file for postgresql java connection here:

> https://jdbc.postgresql.org/download.html

Or you can use a different database but for this to work, change:
```
DriverManager.getConnection("jdbc:database://host:port/database-name", "user-name", "password");
```
for postgresql:
```
DriverManager.getConnection("jdbc:postgresql://localhost:5432/db", "postgres", "password");
```
---

**And finally, in order not to get a database error, you should add the following tables to the database:**
```
CREATE TABLE IF NOT EXISTS auth
(
    id smallint NOT NULL DEFAULT nextval('admin_id_seq'::regclass),
    username character varying COLLATE pg_catalog."default",
    pass character varying COLLATE pg_catalog."default",
    CONSTRAINT admin_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS employer
(
    id integer NOT NULL DEFAULT nextval('employer_id_seq'::regclass),
    fname character varying(255) COLLATE pg_catalog."default",
    lname character varying(255) COLLATE pg_catalog."default",
    tel text[] COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT employer_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS invoice
(
    id integer NOT NULL DEFAULT nextval('invoice_id_seq'::regclass),
    job_id integer,
    amount numeric,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT invoice_pkey PRIMARY KEY (id),
    CONSTRAINT invoice_job_id_fkey FOREIGN KEY (job_id)
        REFERENCES public.job (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS job
(
    id integer NOT NULL DEFAULT nextval('job_id_seq'::regclass),
    employer_id integer,
    price_id integer,
    title character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT job_pkey PRIMARY KEY (id),
    CONSTRAINT job_employer_id_fkey FOREIGN KEY (employer_id)
        REFERENCES public.employer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT job_price_id_fkey FOREIGN KEY (price_id)
        REFERENCES public.price (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS payment
(
    id integer NOT NULL DEFAULT nextval('payment_id_seq'::regclass),
    worker_id integer,
    job_id integer,
    paytype_id integer,
    amount numeric,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT payment_pkey PRIMARY KEY (id),
    CONSTRAINT payment_job_id_fkey FOREIGN KEY (job_id)
        REFERENCES public.job (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT payment_paytype_id_fkey FOREIGN KEY (paytype_id)
        REFERENCES public.paytype (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT payment_worker_id_fkey FOREIGN KEY (worker_id)
        REFERENCES public.worker (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS paytype
(
    id integer NOT NULL DEFAULT nextval('paytype_id_seq'::regclass),
    title character varying(255) COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT paytype_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS price
(
    id integer NOT NULL DEFAULT nextval('price_id_seq'::regclass),
    fulltime numeric,
    halftime numeric,
    overtime numeric,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT price_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS work
(
    id integer NOT NULL DEFAULT nextval('work_id_seq'::regclass),
    job_id integer,
    worker_id integer,
    worktype_id integer,
    workgroup_id integer,
    description text COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT work_pkey PRIMARY KEY (id),
    CONSTRAINT work_job_id_fkey FOREIGN KEY (job_id)
        REFERENCES public.job (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT work_worker_id_fkey FOREIGN KEY (worker_id)
        REFERENCES public.worker (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT work_workgroup_id_fkey FOREIGN KEY (workgroup_id)
        REFERENCES public.workgroup (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT work_worktype_id_fkey FOREIGN KEY (worktype_id)
        REFERENCES public.worktype (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS worker
(
    id integer NOT NULL DEFAULT nextval('worker_id_seq'::regclass),
    fname character varying(255) COLLATE pg_catalog."default",
    lname character varying(255) COLLATE pg_catalog."default",
    tel text[] COLLATE pg_catalog."default",
    iban character varying(30) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT worker_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS workgroup
(
    id integer NOT NULL DEFAULT nextval('workgroop_id_seq'::regclass),
    job_id integer,
    worktype_id integer,
    workcount integer,
    description text COLLATE pg_catalog."default",
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT workgroop_pkey PRIMARY KEY (id),
    CONSTRAINT workgroop_job_id_fkey FOREIGN KEY (job_id)
        REFERENCES public.job (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT workgroop_worktype_id_fkey FOREIGN KEY (worktype_id)
        REFERENCES public.worktype (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

CREATE TABLE IF NOT EXISTS worktype
(
    id integer NOT NULL DEFAULT nextval('worktype_id_seq'::regclass),
    title character varying(255) COLLATE pg_catalog."default",
    no integer,
    date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT worktype_pkey PRIMARY KEY (id)
)

```

