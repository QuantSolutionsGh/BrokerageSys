/* It's an intersting way JPA does the mapping. Note that if in our Java domain, we have a field like
firstName, this translates as first_Name or first_name in our sql script file.

*/

create table users (
    id serial  not null ,
    username varchar(50) not null unique,
    fullname varchar(100) not null,
    password varchar(100) not null,

    enabled int,

    primary key (id)
);

create table user_roles(
    id serial  not null ,
    user_id int not null,
    role varchar(50) not NULL,
    PRIMARY KEY (id),
    CONSTRAINT user_userroles_fk FOREIGN KEY (user_id) REFERENCES users(id)

);

CREATE TABLE Customer (
    id serial  not null ,
    last_Name VARCHAR(100) NOT NULL,
    first_Name VARCHAR(100),
    email VARCHAR(255),
    phone VARCHAR(20),
    address_line1 VARCHAR(255),
    address_line2 VARCHAR(255),
    address_line3 VARCHAR(255),
    other_Names varchar(100),
    PRIMARY KEY (id)
);

CREATE TABLE Sales_Agent(
    id serial NOT NULL   ,
    agent_Name varchar(255) not null,
    PRIMARY KEY (id)
);


CREATE TABLE Insurer(
    id serial not null ,
    company_Name varchar(255) not null,
    primary key(id)

);

create Table Product(
    id serial not null,
    name varchar(50) not null,
    primary key (id)

);


CREATE TABLE Policy (
    id serial  not null ,
    customer_id int not null,
    insurer_id int ,
    policy_Number VARCHAR(100) NOT NULL,
    policy_Type VARCHAR(100),
    cover_Fm_Date DATE NOT NULL,
    cover_To_Date DATE NOT NULL,
    transaction_Date DATE not null,
    insurance_Company VARCHAR(255) ,
    premium numeric,
    currency varchar(10) not null,
    amt_paid numeric,
    commission_rate numeric,
    agent_id int,
    product_id int,
    PRIMARY KEY (id),
    constraint policy_customer_fk FOREIGN KEY (customer_id) REFERENCES customer(id),
    constraint policy_sales_agent_fk FOREIGN KEY (agent_id) references Sales_Agent(id),
    constraint policy_insurer_fk foreign key (insurer_id) references Insurer(id),
    constraint policy_product_fk FOREIGN KEY (product_id) REFERENCES Product(id)

);


create Table Report(
    id serial not null  ,
    report_id varchar(50) not null,
    report_description varchar(300) not null,
    PRIMARY KEY (id)

);



CREATE TABLE Payment_Details (
    id serial NOT NULL   ,
    policy_id int not null,
    amt_Paid numeric,
    transaction_Date DATE,
    entry_date date not null,
    comments varchar(100),
    PRIMARY KEY (id),
    constraint paymentDetails_policy_fk FOREIGN KEY (policy_id) REFERENCES Policy(id)
);




insert into Sales_Agent(id,agent_Name) values (nextval('sales_agent_id_seq'), 'TEST 1');

insert into Sales_Agent(id,agent_Name) values (nextval('sales_agent_id_seq'), 'TEST 2');

insert into Insurer(id,company_Name) values (nextval('insurer_id_seq'),'HOLLARD INSURANCE');

insert into Insurer(id, company_Name) values (nextval('insurer_id_seq'),'ENTERPRISE INSURANCE');

insert into Insurer(id,company_name) values(nextval('insurer_id_seq'),'DONEWELL INSURANCE');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'prospective_clients.rptdesign',
                                                           'This report lists all clients without any policies recorded.');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'production.rptdesign',
                                                           'This report shows the production report for a specified period.');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'lapsedpolicies.rptdesign',
                                                           'This report lists all policies due for renewal within a specified period.');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'commission_payable.rptdesign',
                                                           'This report lists commission payable for agents within a specified period. Calculation is based on premium production');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'commission_receipts.rptdesign',
                                                           'This report lists commission for agents within a specified period. Calculation is based on premium receipts');

insert into Report(id,report_id,report_description) values(nextval('report_id_seq'),'analysis_box.rptdesign',
                                                           'TODO - production by product, by insurer, by agents, policies per intermediary etc ');



insert into users (id,username,password,enabled,fullname)
values (nextval('users_id_seq'),'admin','$2a$10$E3mPTZb50e7sSW15fDx8Ne7hDZpfDjrmMPTTUp8wVjLTu.G5oPYCO',1,'administrator');



insert into user_roles(id,user_id,role) values(nextval('user_roles_id_seq'),CURRVAL('users_id_seq'),'ROLE_DEVELOPER');

insert into users (id,username,password,enabled,fullname)
values (nextval('users_id_seq'),'albert','$2a$10$7JTVlDqJXnWoalwC1dP7E.AtVYvLV/Ur3YzptrfrnubKFGg5T3JY6',1,'albert');

insert into user_roles(id,user_id,role) values(nextval('user_roles_id_seq'),CURRVAL('users_id_seq'),'ROLE_DEVELOPER');


insert into Product(id,name) values(nextval('product_id_seq'),'FIRE');

insert into Product(id,name) values(nextval('product_id_seq'),'MOTOR');

insert into Product(id,name) values(nextval('product_id_seq'),'MARINE');

insert into Product(id,name) values(nextval('product_id_seq'),'ENGINEERING');

insert into Product(id,name) values(nextval('product_id_seq'),'GENERAL ACCIDENT');

insert into Product(id,name) values(nextval('product_id_seq'),'BONDS');

insert into Product(id,name) values(nextval('product_id_seq'),'OTHERS');






