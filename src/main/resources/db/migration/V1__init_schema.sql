CREATE TABLE public.invalidated_token (
    id character varying(255) NOT NULL,
    exp_time timestamp(6) without time zone
);

CREATE TABLE public.outbox (
    id character varying(255) NOT NULL,
    aggregate_id character varying(255) NOT NULL,
    aggregate_type character varying(255) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    event_type character varying(255) NOT NULL,
    payload text NOT NULL,
    status character varying(255) NOT NULL,
    CONSTRAINT outbox_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PROCESSED'::character varying, 'FAILED'::character varying])::text[])))
);

CREATE TABLE public.permission (
    name character varying(255) NOT NULL,
    description character varying(255)
);

CREATE TABLE public.role (
    name character varying(255) NOT NULL,
    description character varying(255)
);

CREATE TABLE public.role_permission (
    role_name character varying(255) NOT NULL,
    permission_name character varying(255) NOT NULL
);

CREATE TABLE public.tasks (
    id character varying(255) NOT NULL,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_by character varying(255),
    updated_at timestamp(6) without time zone,
    deadline timestamp(6) without time zone,
    status character varying(255),
    title character varying(255),
    warning_email_sent boolean,
    user_id character varying(255),
    CONSTRAINT tasks_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'IN_PROGRESS'::character varying, 'COMPLETED'::character varying])::text[])))
);

CREATE TABLE public.user_role (
    user_id character varying(255) NOT NULL,
    role_name character varying(255) NOT NULL
);

CREATE TABLE public.users (
    id character varying(255) NOT NULL,
    created_at timestamp(6) without time zone,
    created_by character varying(255),
    last_modified_by character varying(255),
    updated_at timestamp(6) without time zone,
    email character varying(255),
    password character varying(255),
    username character varying(255) NOT NULL
);

ALTER TABLE ONLY public.invalidated_token
    ADD CONSTRAINT invalidated_token_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.outbox
    ADD CONSTRAINT outbox_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (name);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT role_permission_pkey PRIMARY KEY (role_name, permission_name);

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (name);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_name);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

CREATE INDEX idx_task_user_id ON public.tasks USING btree (user_id);
CREATE INDEX idx_task_user_status ON public.tasks USING btree (user_id, status);

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk6s1ob9k4ihi75xbxe2w0ylsdh FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkj345gk1bovqvfame88rcx7yyx FOREIGN KEY (user_id) REFERENCES public.users(id);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fkldkc3yoh80x16gv94519otli4 FOREIGN KEY (permission_name) REFERENCES public.permission(name);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkn6r4465stkbdy93a9p8cw7u24 FOREIGN KEY (role_name) REFERENCES public.role(name);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fkng6lj87lo2uxina096lfjpdlp FOREIGN KEY (role_name) REFERENCES public.role(name);
