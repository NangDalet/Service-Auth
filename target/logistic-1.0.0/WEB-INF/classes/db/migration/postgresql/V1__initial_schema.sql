-- ============================================================
-- PostgreSQL Migration Script
-- Converted from MySQL student_register database
-- ============================================================

-- ============================================================
-- Enable Extensions
-- ============================================================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================
-- Drop tables in correct order (foreign key dependencies)
-- ============================================================
DROP TABLE IF EXISTS access_token CASCADE;
DROP TABLE IF EXISTS branch_currencies CASCADE;
DROP TABLE IF EXISTS branches CASCADE;
DROP TABLE IF EXISTS branch_types CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS currency_centers CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS deposits CASCADE;
DROP TABLE IF EXISTS exchange_rates CASCADE;
DROP TABLE IF EXISTS groups CASCADE;
DROP TABLE IF EXISTS invoices CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS modules CASCADE;
DROP TABLE IF EXISTS module_types CASCADE;
DROP TABLE IF EXISTS oauth_client_details CASCADE;
DROP TABLE IF EXISTS payment CASCADE;
DROP TABLE IF EXISTS payment_methods CASCADE;
DROP TABLE IF EXISTS permissions CASCADE;
DROP TABLE IF EXISTS pgroup_companies CASCADE;
DROP TABLE IF EXISTS positions CASCADE;
DROP TABLE IF EXISTS programs CASCADE;
DROP TABLE IF EXISTS provinces CASCADE;
DROP TABLE IF EXISTS receipts CASCADE;
DROP TABLE IF EXISTS receipts_deposit CASCADE;
DROP TABLE IF EXISTS receive_payment_details CASCADE;
DROP TABLE IF EXISTS refund_history CASCADE;
DROP TABLE IF EXISTS refun_deposits CASCADE;
DROP TABLE IF EXISTS registration CASCADE;
DROP TABLE IF EXISTS registration_course CASCADE;
DROP TABLE IF EXISTS shifts CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS term_conditions CASCADE;
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS type_payments CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_branches CASCADE;
DROP TABLE IF EXISTS user_groups CASCADE;

-- ============================================================
-- Table: access_token
-- ============================================================
CREATE TABLE access_token (
                              id BIGSERIAL PRIMARY KEY,
                              username VARCHAR(255),
                              login_verify_token VARCHAR(255),
                              access_token TEXT,
                              token_type VARCHAR(255),
                              refresh_token TEXT,
                              expires_in BIGINT,
                              scope VARCHAR(255),
                              created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              created_by VARCHAR(255),
                              is_active SMALLINT DEFAULT 1,
                              email VARCHAR(255),
                              user_id INTEGER,
                              session_id VARCHAR(255) NOT NULL
);

-- ============================================================
-- Table: branches
-- ============================================================
CREATE TABLE branches (
                          id SERIAL PRIMARY KEY,
                          sys_code VARCHAR(50),
                          company_id INTEGER,
                          branch_type_id INTEGER,
                          name VARCHAR(150),
                          name_other VARCHAR(150),
                          telephone VARCHAR(255),
                          email_address VARCHAR(50),
                          fax_number VARCHAR(50),
                          long VARCHAR(55),
                          lat VARCHAR(55),
                          country_id INTEGER,
                          province_id INTEGER,
                          district_id INTEGER,
                          commune_id INTEGER,
                          village_id INTEGER,
                          address TEXT,
                          address_other TEXT,
                          currency_center_id INTEGER,
                          pos_currency_id INTEGER,
                          work_start TIME,
                          work_end TIME,
                          created TIMESTAMP,
                          created_by INTEGER,
                          modified TIMESTAMP,
                          modified_by INTEGER,
                          act SMALLINT DEFAULT 1,
                          is_head SMALLINT DEFAULT 0,
                          is_active SMALLINT DEFAULT 1,
                          status SMALLINT
);

CREATE INDEX idx_branches_is_head ON branches(is_head);
CREATE INDEX idx_branches_search ON branches(name, is_active, company_id);
CREATE INDEX idx_branches_sys_code ON branches(sys_code);

-- ============================================================
-- Data: branches
-- ============================================================
INSERT INTO branches (id, sys_code, company_id, branch_type_id, name, name_other, telephone, email_address, fax_number, long, lat, country_id, province_id, district_id, commune_id, village_id, address, address_other, currency_center_id, pos_currency_id, work_start, work_end, created, created_by, modified, modified_by, act, is_head, is_active, status) VALUES
                                                                                                                                                                                                                                                                                                                                                                     (1, '008dd5fb9270a1ead2c83e6044841bd9', 1, 1, 'BLUE BATTAMBANG', 'សាកលវិទ្យាល័យប្លូបាត់ដំបង', '012 / 015 / 011 123 456', 'testbtb@gmail.com', '0231112225', '15.33', '25.123', 36, NULL, NULL, NULL, NULL, 'Battambang City', 'ក្រុងបាត់ដំបង', 1, 1, '08:00:00', '17:00:00', '2017-08-22 11:41:32', 1, '2026-01-17 09:37:49', 1, 1, 1, 2, 1),
                                                                                                                                                                                                                                                                                                                                                                     (4, 'fda0c0aa-34b8-11f0-b62a-00155dd15877', 1, NULL, 'BLUE Bonteaymeanchey', 'សាកលវិទ្យាល័យប្លូបន្ទាយមានជ័យ', '012666999', 'umebonteaymeanchey@gmail.com', '012666999', '12', '22', NULL, NULL, NULL, NULL, NULL, 'Bonteay Meanchey', 'បន្ទាយមានជ័យ', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-14 14:28:39', 1, 1, 0, 2, 1),
                                                                                                                                                                                                                                                                                                                                                                     (5, 'c256458f-34bb-11f0-b62a-00155dd15877', 1, NULL, 'BLUE Porsat', 'សាកលវិទ្យាល័យគ្រប់គ្រង នឹង សេដ្ឋកិច្ច ពោធិ៍សាត់', '011222333', 'umeporsat@gmail.com', '011222333', '25', '36', NULL, NULL, NULL, NULL, NULL, 'address​ English', 'addressខ្មែរ', 1, NULL, '08:00:00', '20:00:00', NULL, 1, '2026-01-14 09:08:15', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (6, 'b1b71fd1-34bc-11f0-b62a-00155dd15877', 1, NULL, 'BLUE Koh Kong', 'សាកលវិទ្យាល័យតេស​ កោះកុង', '015444777', 'umekohkong@gmail.com', '015444777', '25', '36', NULL, NULL, NULL, NULL, NULL, 'address​ English', 'addressខ្មែរ', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-13 20:37:46', 1, 1, 0, 2, 1),
                                                                                                                                                                                                                                                                                                                                                                     (7, '5f5681d0-4059-11f0-a785-c8940213ae2e', 1, NULL, 'BLUE Kompot', 'សកលវិទ្យាល័យប្លូ កំពត', '010 222 333', 'umekompot@gmail.com', '012', '12', '15', NULL, NULL, NULL, NULL, NULL, 'asd', 'asd', 1, NULL, '08:00:00', '20:00:00', NULL, 1, '2026-01-14 11:27:25', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (8, '962933f7-405a-11f0-a785-c8940213ae2e', 1, NULL, 'BLUE Kompong Chhnang', 'សាកលវិទ្យាល័យប្លូ កំពង់ឆ្នាំង', '016 444 555', 'umekompot@gmail.com', '011', '12', '15', NULL, NULL, NULL, NULL, NULL, 'gg', 'gg', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-14 12:27:07', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (9, '73c8542b-4080-11f0-a785-c8940213ae2e', 1, NULL, 'BLUE Kompongcham', 'សាកលវិទ្យាល័យប្លូ កំពង់ចាម', '012 777 888', 'bluekpc@gmail.com', '', '', '', NULL, NULL, NULL, NULL, NULL, '', '', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-13 20:38:25', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (10, '8746f7e5-4080-11f0-a785-c8940213ae2e', 1, NULL, 'BLUE SiemReap', 'សាកលវិទ្យាល័យគ្រប់គ្រង នឹង សេដ្ឋកិច្ច សៀមរាប', '011 444 555', 'bluesr@gmail.com', '', '', '', NULL, NULL, NULL, NULL, NULL, 'a', 'a', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-14 09:12:14', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (12, 'b86e45a4-4080-11f0-a785-c8940213ae2e', 1, NULL, 'BLUE KompongThom', 'កំពង់ធំ', '015 111 222', 'kompongthom@gmail.com', '', '', '', NULL, NULL, NULL, NULL, NULL, '', 'កំពង់ធំ', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-14 09:13:40', 1, 1, 0, 1, 0),
                                                                                                                                                                                                                                                                                                                                                                     (13, 'd5b94d87-4080-11f0-a785-c8940213ae2e', 1, NULL, 'UME PreyVeng', 'សាកលវិទ្យាល័យប្លូ ព្រៃវែង', '096 111 2574', 'preyveng@gmail.com', '02305577', '12', '22', NULL, NULL, NULL, NULL, NULL, '', 'សាកលវិទ្យាល័យប្លូ ព្រៃវែង', 1, NULL, '08:00:00', '17:00:00', NULL, 1, '2026-01-17 09:39:24', 1, 1, 0, 1, 1),
                                                                                                                                                                                                                                                                                                                                                                     (22, '6ccca0df-f111-11f0-b0bd-00155d5ceaaf', 1, NULL, 'Blue Presihanuk', 'សាកលវិទ្យាល័យប្លូ ព្រះសីហនុ', '012777888', 'nuk@gmail.com', '023999888', '', '', NULL, NULL, NULL, NULL, NULL, 'Presihanuk', 'ក្រុងព្រះសីហនុ', 1, NULL, '08:00:00', '17:00:00', NULL, 1, NULL, NULL, 1, 0, 1, 1);

-- Reset sequence
SELECT setval('branches_id_seq', (SELECT MAX(id) FROM branches));

-- ============================================================
-- Table: branch_currencies
-- ============================================================
CREATE TABLE branch_currencies (
                                   id SERIAL PRIMARY KEY,
                                   sys_code VARCHAR(50),
                                   branch_id INTEGER,
                                   currency_center_id INTEGER,
                                   exchange_rate_id INTEGER,
                                   rate_to_sell DECIMAL(15,9) DEFAULT 0.000000000,
                                   rate_to_change DECIMAL(15,9) DEFAULT 0.000000000,
                                   rate_purchase DECIMAL(15,9) DEFAULT 0.000000000,
                                   created TIMESTAMP,
                                   created_by BIGINT,
                                   modified TIMESTAMP,
                                   modified_by BIGINT,
                                   is_pos_default SMALLINT DEFAULT 0,
                                   is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_branch_currencies_search ON branch_currencies(branch_id, currency_center_id);
CREATE INDEX idx_branch_currencies_sys_code ON branch_currencies(sys_code);

INSERT INTO branch_currencies (id, sys_code, branch_id, currency_center_id, exchange_rate_id, rate_to_sell, rate_to_change, rate_purchase, created, created_by, modified, modified_by, is_pos_default, is_active) VALUES
    (1, '041826786bd9fefcf564c9e010f0d6b6', 1, 2, 1, 4100.000000000, 4050.000000000, 4100.000000000, '2017-11-06 15:37:53', 1, '2017-11-07 11:29:23', 1, 1, 1);

SELECT setval('branch_currencies_id_seq', (SELECT MAX(id) FROM branch_currencies));

-- ============================================================
-- Table: branch_types
-- ============================================================
CREATE TABLE branch_types (
                              id SERIAL PRIMARY KEY,
                              sys_code VARCHAR(50),
                              name VARCHAR(255),
                              created TIMESTAMP,
                              created_by INTEGER,
                              modified TIMESTAMP,
                              modified_by INTEGER,
                              is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_branch_types_sys_code ON branch_types(sys_code);
CREATE INDEX idx_branch_types_search ON branch_types(name, is_active);

INSERT INTO branch_types (id, sys_code, name, created, created_by, modified, modified_by, is_active) VALUES
    (1, '108de0138a3c639a32e153a442560811', 'Branch', '2016-11-04 13:49:31', 1, '2016-11-04 13:49:31', NULL, 1);

SELECT setval('branch_types_id_seq', (SELECT MAX(id) FROM branch_types));

-- ============================================================
-- Table: course
-- ============================================================
CREATE TABLE course (
                        id BIGSERIAL PRIMARY KEY,
                        program_id BIGINT,
                        code VARCHAR(50) NOT NULL UNIQUE,
                        name VARCHAR(200) NOT NULL,
                        credit INTEGER NOT NULL
);

INSERT INTO course (id, program_id, code, name, credit) VALUES
                                                            (1, 1, 'IT101', 'Introduction to Programming', 3),
                                                            (2, 1, 'IT102', 'Database Systems', 3),
                                                            (3, 2, 'SE201', 'Software Design Patterns', 4),
                                                            (4, 3, 'BA101', 'Principles of Management', 3),
                                                            (5, 3, 'BA102', 'Financial Accounting', 3);

SELECT setval('course_id_seq', (SELECT MAX(id) FROM course));

-- ============================================================
-- Table: currency_centers
-- ============================================================
CREATE TABLE currency_centers (
                                  id SERIAL PRIMARY KEY,
                                  sys_code VARCHAR(50),
                                  name VARCHAR(50),
                                  symbol VARCHAR(50),
                                  photo VARCHAR(50),
                                  created TIMESTAMP,
                                  created_by BIGINT,
                                  modified TIMESTAMP,
                                  modified_by BIGINT,
                                  is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_currency_centers_search ON currency_centers(name, is_active);
CREATE INDEX idx_currency_centers_sys_code ON currency_centers(sys_code);

INSERT INTO currency_centers (id, sys_code, name, symbol, photo, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                                            (1, 'c3691f6f110a04bc03f49ff1b6be11c2', 'USD - US Dollar', '$', NULL, '2015-12-16 13:43:33', 1, '2015-12-16 13:43:33', NULL, 1),
                                                                                                                            (2, '50dbcae7b49c7ba293200a3df51b23eb', 'KHR - Cambodia Riel', '៛', NULL, '2015-12-16 13:44:48', 1, '2015-12-16 13:44:48', NULL, 1),
                                                                                                                            (3, '5fed42699043f9baba7e518941dafa90', 'THB - Thai Baht', '฿', NULL, '2015-12-16 13:45:56', 1, '2015-12-16 13:45:56', NULL, 1),
                                                                                                                            (4, '74305edde4cd955d64ab68229714e719', 'VND - Viet Nam Dong', '₫', NULL, '2015-12-16 13:47:36', 1, '2015-12-16 13:47:36', NULL, 1),
                                                                                                                            (5, 'abea1ec3126d23d3eda701ecc0a1dcd7', 'MYR - Malaysia Ringgit', 'RM', NULL, '2015-12-16 13:48:12', 1, '2015-12-16 13:48:12', NULL, 1),
                                                                                                                            (6, 'bb5cfa9f1bfad3cb7a7a749870181e6e', 'LAK - Laos Kip', '₭', NULL, '2015-12-16 13:49:50', 1, '2015-12-16 13:51:55', NULL, 1),
                                                                                                                            (7, '1571d082a6c3362c83785d28a56856f8', 'SGD - Singapore Dollar', '$', NULL, '2015-12-16 13:51:00', 1, '2015-12-16 13:52:42', NULL, 1),
                                                                                                                            (8, '9b50c6bfb5aec95e1d10ed7b6a5398a3', 'PHP - Philippines Peso', '₱', NULL, '2015-12-16 13:51:40', 1, '2015-12-16 13:51:40', NULL, 1),
                                                                                                                            (9, '2926eeaaaeaa0873afcee0f6480e56b4', 'IDR - Indonesia Rupiah', 'Rp', NULL, '2015-12-16 13:53:30', 1, '2015-12-16 13:53:30', NULL, 1),
                                                                                                                            (10, '983db8739b2fdf6ebffbff1b750a8e8c', 'BND - Brunei Darussalam Dollar', '$', NULL, '2015-12-16 13:54:40', 1, '2015-12-16 13:54:40', NULL, 1),
                                                                                                                            (11, '5e41b3512e59058fc1d631133f3df8f8', 'CNY - China Yuan Renminbi', '¥', NULL, '2015-12-17 15:11:01', 1, '2015-12-17 15:11:01', NULL, 1),
                                                                                                                            (12, '0b81d08dc7383cef91a806761cca7f72', 'JPY - Japan Yen', '¥', NULL, '2015-12-17 15:13:56', 1, '2015-12-17 15:13:56', NULL, 1),
                                                                                                                            (13, '18db72eada1a4375c5a916de6d7ecb5c', 'KRW - Korea (South) Won', '₩', NULL, '2015-12-17 15:15:31', 1, '2015-12-17 15:15:31', NULL, 1);

SELECT setval('currency_centers_id_seq', (SELECT MAX(id) FROM currency_centers));

-- ============================================================
-- Table: departments
-- ============================================================
CREATE TABLE departments (
                             id SERIAL PRIMARY KEY,
                             sys_code VARCHAR(100),
                             company_id INTEGER,
                             name VARCHAR(255) UNIQUE,
                             created TIMESTAMP,
                             created_by BIGINT,
                             modified TIMESTAMP,
                             modified_by BIGINT,
                             is_active SMALLINT DEFAULT 1
);

INSERT INTO departments (id, sys_code, company_id, name, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                                    (1, 'CS', 1, 'Computer Science', '2016-07-20 10:52:15', 1, '2016-07-20 10:52:15', NULL, 1),
                                                                                                                    (2, 'BUS', 1, 'Business Administration', '2016-07-20 10:53:14', 1, '2016-07-20 10:57:00', 1, 1),
                                                                                                                    (3, NULL, 1, 'Surgery', '2016-07-20 10:53:25', 1, '2016-07-20 10:53:25', NULL, 1),
                                                                                                                    (4, NULL, 1, 'Marketing', '2016-07-20 10:53:39', 1, '2016-07-20 10:53:39', NULL, 1),
                                                                                                                    (5, NULL, 1, 'Law', '2016-07-20 10:53:57', 1, '2016-07-20 10:56:44', 1, 1),
                                                                                                                    (6, NULL, 1, 'IT', '2016-07-20 10:54:07', 1, '2016-07-20 10:54:07', NULL, 1),
                                                                                                                    (7, NULL, 1, 'AR', '2016-07-20 10:54:35', 1, '2016-07-20 10:54:35', NULL, 1),
                                                                                                                    (8, NULL, 1, 'Account', '2016-07-20 10:54:49', 1, '2016-07-20 10:54:49', NULL, 1),
                                                                                                                    (9, NULL, 2, 'string', '2025-06-17 14:17:50', 1, '2025-06-17 14:21:11', 1, 1);

SELECT setval('departments_id_seq', (SELECT MAX(id) FROM departments));

-- ============================================================
-- Table: deposits
-- ============================================================
CREATE TABLE deposits (
                          id BIGSERIAL PRIMARY KEY,
                          invoice_no VARCHAR(50) UNIQUE NOT NULL,
                          stu_name VARCHAR(150) NOT NULL,
                          stu_phone VARCHAR(50),
                          posting_date TIMESTAMP NOT NULL,
                          remark VARCHAR(255),
                          total DECIMAL(18,2) DEFAULT 0.00,
                          deposit DECIMAL(18,2) DEFAULT 0.00,
                          balance_due DECIMAL(18,2) DEFAULT 0.00,
                          created_by VARCHAR(50),
                          created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          modified_by VARCHAR(50),
                          modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table: exchange_rates
-- ============================================================
CREATE TABLE exchange_rates (
                                id BIGSERIAL PRIMARY KEY,
                                sys_code VARCHAR(50),
                                branch_id INTEGER,
                                currency_center_id INTEGER,
                                rate_to_sell DECIMAL(15,9) DEFAULT 0.000000000,
                                rate_to_change DECIMAL(15,9) DEFAULT 0.000000000,
                                rate_purchase DECIMAL(15,9) DEFAULT 0.000000000,
                                created TIMESTAMP,
                                created_by BIGINT,
                                modified TIMESTAMP,
                                modified_by BIGINT,
                                is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_exchange_rates_currency ON exchange_rates(currency_center_id);
CREATE INDEX idx_exchange_rates_branch ON exchange_rates(branch_id);
CREATE INDEX idx_exchange_rates_sys_code ON exchange_rates(sys_code);

INSERT INTO exchange_rates (id, sys_code, branch_id, currency_center_id, rate_to_sell, rate_to_change, rate_purchase, created, created_by, modified, modified_by, is_active) VALUES
    (1, 'EX123', 1, 1, 4000.000000000, 3950.000000000, 3900.000000000, '2025-01-29 16:11:44', 1, '2025-01-29 16:11:44', 1, 1);

SELECT setval('exchange_rates_id_seq', (SELECT MAX(id) FROM exchange_rates));

-- ============================================================
-- Table: groups
-- ============================================================
CREATE TABLE groups (
                        id SERIAL PRIMARY KEY,
                        sys_code VARCHAR(100),
                        name VARCHAR(255),
                        type SMALLINT,
                        price DECIMAL(15,3),
                        created TIMESTAMP,
                        created_by BIGINT,
                        modified TIMESTAMP,
                        modified_by BIGINT,
                        is_active SMALLINT DEFAULT 1
);

INSERT INTO groups (id, sys_code, name, type, price, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                                (1, '231341411233', 'Admin', NULL, NULL, '2017-02-17 09:31:52', 1, '2026-01-15 20:49:09', 30, 1),
                                                                                                                (2, '9a386278fd59477b67008b4e44b7ff77', 'IT', NULL, NULL, '2017-12-23 10:46:34', 1, '2024-06-12 16:42:17', 1, 1),
                                                                                                                (3, 'b25e41d5789b25e3d7c4f966412cc434', 'AR', NULL, NULL, '2018-09-14 10:11:45', 8, '2026-01-15 20:36:35', 1, 2),
                                                                                                                (4, '0be25c96cf5a77341b941a48dd3d3504', 'super_admin', NULL, NULL, '2018-09-14 10:14:35', 1, '2026-01-15 11:40:33', 1, 1),
                                                                                                                (5, '1fccc216e174ce00c625d25292f97dda', 'Account', NULL, NULL, '2020-03-18 16:58:23', 5, '2026-01-15 20:37:05', 1, 2),
                                                                                                                (12, 'e76269a3e6dfb22224fa2ca1d48267e6', 'Student', NULL, NULL, '2025-08-28 14:37:58', 1, '2025-08-28 15:43:06', 1, 1),
                                                                                                                (13, '73bba040f5bb46f17784d7be64323fc8', 'Branch', NULL, NULL, '2025-09-29 14:46:19', 1, '2025-09-29 14:58:29', 1, 1),
                                                                                                                (14, 'e32dfeb11a1d0ce2c4da650278c7d1bb', 'Test', NULL, NULL, '2026-01-15 10:48:09', 1, '2026-01-15 14:29:38', 1, 2),
                                                                                                                (15, '5a5b1040aa0be366a552bd6149673db3', 'AP', NULL, NULL, '2026-01-15 11:06:41', 1, '2026-01-15 14:29:26', 1, 2),
                                                                                                                (16, '3699d39c8902d894db755a274ffe76d2', 'QA', NULL, NULL, '2026-01-15 11:21:01', 1, '2026-01-15 13:44:35', 1, 2),
                                                                                                                (19, '5937da938e73e16c6c60793a10a87cb2', 'WE', NULL, NULL, '2026-01-15 14:36:38', 1, '2026-01-15 14:38:33', 1, 2),
                                                                                                                (21, '70a15505632530784c85de64a05e351e', 'sa', NULL, NULL, '2026-01-15 14:39:48', 1, '2026-01-15 14:40:36', 1, 2),
                                                                                                                (23, '03a155a5a43c89a837b46359eab953a1', 'Account', NULL, NULL, '2026-01-15 20:47:42', 1, NULL, NULL, 1),
                                                                                                                (24, '026eacd0e9a8860c138912b87dfa7291', 'AR', NULL, NULL, '2026-01-15 20:48:36', 30, '2026-01-15 20:48:53', 30, 1);

SELECT setval('groups_id_seq', (SELECT MAX(id) FROM groups));

-- ============================================================
-- Table: invoices
-- ============================================================
CREATE TABLE invoices (
                          id BIGSERIAL PRIMARY KEY,
                          invoice_code VARCHAR(255),
                          company_id INTEGER,
                          branch_id INTEGER,
                          student_id INTEGER,
                          program_id INTEGER,
                          semester VARCHAR(50),
                          academic_year VARCHAR(50),
                          unit_price DOUBLE PRECISION DEFAULT 0,
                          discount_percent DOUBLE PRECISION DEFAULT 0,
                          discount_amount DOUBLE PRECISION DEFAULT 0,
                          balance DOUBLE PRECISION DEFAULT 0,
                          total_amount DOUBLE PRECISION DEFAULT 0,
                          status SMALLINT,
                          created TIMESTAMP,
                          created_by INTEGER,
                          modified TIMESTAMP,
                          modified_by INTEGER
);

CREATE INDEX idx_invoices_invoice_code ON invoices(invoice_code);

INSERT INTO invoices (id, invoice_code, company_id, branch_id, student_id, program_id, semester, academic_year, unit_price, discount_percent, discount_amount, balance, total_amount, status, created, created_by, modified, modified_by) VALUES
                                                                                                                                                                                                                                              (26, '20250905001', NULL, NULL, 32, 1, '2', '22', 500, 0, 0, NULL, 500, 1, '2025-09-05 11:17:13', 1, NULL, NULL),
                                                                                                                                                                                                                                              (27, '20250905001', NULL, NULL, 33, 1, '2', '2', 500, 0, 0, NULL, 500, 1, '2025-09-05 15:00:46', 1, NULL, NULL),
                                                                                                                                                                                                                                              (28, '20250905001', NULL, NULL, 34, 1, '2', '2', 500, 0, 0, NULL, 500, 1, '2025-09-05 16:11:19', 1, NULL, NULL),
                                                                                                                                                                                                                                              (29, '20250909001', NULL, NULL, 35, 1, '2', '22', 500, 0, 0, NULL, 500, 1, '2025-09-09 14:50:22', 1, NULL, NULL),
                                                                                                                                                                                                                                              (30, '20250917001', NULL, NULL, 36, 1, '2', '42', 500, 0, 0, NULL, 500, 1, '2025-09-17 20:40:19', 1, NULL, NULL),
                                                                                                                                                                                                                                              (31, '20260117001', NULL, NULL, 37, 0, '', '2026', 500, 10, 50, 450, 450, 1, '2026-01-17 09:21:51', 1, NULL, NULL),
                                                                                                                                                                                                                                              (32, '20260117001', NULL, NULL, 38, 0, '', '2026', 1500, 15, 225, 1275, 1275, 1, '2026-01-17 09:51:39', 1, NULL, NULL),
                                                                                                                                                                                                                                              (33, '20260118001', NULL, NULL, 39, 0, '', '2026', 1500, 10, 150, 1350, 1350, 1, '2026-01-18 08:44:47', 1, NULL, NULL),
                                                                                                                                                                                                                                              (34, '20260118001', NULL, NULL, 40, 0, '', '2026', 500, 15, 75, 425, 425, 1, '2026-01-18 08:47:47', 1, NULL, NULL),
                                                                                                                                                                                                                                              (35, '20260118001', NULL, NULL, 41, 0, '', '2026', 1500, 10, 150, 1350, 1350, 1, '2026-01-18 08:53:44', 1, NULL, NULL),
                                                                                                                                                                                                                                              (36, '20260118001', NULL, NULL, 42, 0, '', '2026', 1500, 10, 150, 1350, 1350, 1, '2026-01-18 09:25:12', 1, NULL, NULL),
                                                                                                                                                                                                                                              (37, '20260118001', NULL, NULL, 43, 0, '', '2026', 500, 50, 250, 250, 250, 1, '2026-01-18 09:45:45', 1, NULL, NULL),
                                                                                                                                                                                                                                              (38, '20260118001', NULL, NULL, 44, 0, '', '2026', 500, 80, 400, 100, 100, 1, '2026-01-18 10:01:27', 1, NULL, NULL),
                                                                                                                                                                                                                                              (39, '20260118001', NULL, NULL, 44, 0, '', '2026', 1000, 90, 900, 100, 100, 1, '2026-01-18 10:01:27', 1, NULL, NULL),
                                                                                                                                                                                                                                              (40, '20260118001', NULL, NULL, 45, 0, '', '2026', 1500, 90, 1350, 150, 150, 1, '2026-01-18 10:05:06', 1, NULL, NULL),
                                                                                                                                                                                                                                              (41, '20260118001', NULL, NULL, 46, 0, '', '2026', 1500, 90, 1350, 150, 150, 1, '2026-01-18 10:12:35', 1, NULL, NULL),
                                                                                                                                                                                                                                              (42, '20260118001', NULL, NULL, 47, 0, '', '2026', 500, 80, 400, 100, 100, 1, '2026-01-18 10:17:44', 1, NULL, NULL),
                                                                                                                                                                                                                                              (43, '20260121001', NULL, NULL, 48, 0, '', '2026', 1000, 80, 800, 200, 200, 1, '2026-01-21 09:57:38', 1, NULL, NULL),
                                                                                                                                                                                                                                              (44, '20260121001', NULL, NULL, 49, 0, '', '2026', 1500, 20, 300, 1200, 1200, 1, '2026-01-21 10:04:19', 1, NULL, NULL),
                                                                                                                                                                                                                                              (45, '20260121001', NULL, NULL, 50, 0, '', '2026', 1500, 50, 750, 750, 750, 1, '2026-01-21 10:55:27', 1, NULL, NULL),
                                                                                                                                                                                                                                              (46, '20260121001', NULL, NULL, 51, 0, '', '2026', 1000, 50, 500, 500, 500, 1, '2026-01-21 11:10:51', 1, NULL, NULL),
                                                                                                                                                                                                                                              (47, '20260121001', NULL, NULL, 52, 0, '', '2026', 1000, 25, 250, 750, 750, 1, '2026-01-21 11:19:33', 1, NULL, NULL),
                                                                                                                                                                                                                                              (48, '20260121001', NULL, NULL, 53, 0, '', '2026', 500, 49.99, 249.95, 250.05, 250.05, 1, '2026-01-21 11:28:13', 1, NULL, NULL);

SELECT setval('invoices_id_seq', (SELECT MAX(id) FROM invoices));

-- ============================================================
-- Table: locations
-- ============================================================
CREATE TABLE locations (
                           id SERIAL PRIMARY KEY,
                           sys_code VARCHAR(50),
                           location_group_id INTEGER,
                           name VARCHAR(255),
                           color VARCHAR(50),
                           level VARCHAR(50),
                           aisle VARCHAR(50),
                           bay VARCHAR(50),
                           bin VARCHAR(50),
                           position VARCHAR(50),
                           created TIMESTAMP,
                           created_by BIGINT,
                           modified TIMESTAMP,
                           modified_by BIGINT,
                           is_for_sale SMALLINT,
                           is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_locations_name ON locations(name);
CREATE INDEX idx_locations_group ON locations(location_group_id);
CREATE INDEX idx_locations_search ON locations(is_for_sale, is_active);
CREATE INDEX idx_locations_sys_code ON locations(sys_code);

INSERT INTO locations (id, sys_code, location_group_id, name, color, level, aisle, bay, bin, position, created, created_by, modified, modified_by, is_for_sale, is_active) VALUES
                                                                                                                                                                               (1, 'd6d08e5324b88fb71579c3bbcfa5cb17', 1, 'sale', '', '', '', '', '', '', '2017-08-30 13:35:30', 1, '2025-01-14 14:49:12', 1, 1, 1),
                                                                                                                                                                               (2, '9f3882ab80fd12b1fa762c5d0f10a5df', 4, 'Loc-Using', '', '', '', '', '', '', '2020-04-02 15:17:31', 1, '2024-07-18 08:52:34', 1, 1, 1),
                                                                                                                                                                               (24, 'd965a8be-e8ef-11ef-887a-6c2b5979c6e5', 33, 'Warehouse A', 'Red', 'L2', 'A12', 'B3', 'Bin45', 'North', '2025-02-12 10:17:11', 1, NULL, NULL, 1, 1),
                                                                                                                                                                               (25, '17d00614-e8f0-11ef-887a-6c2b5979c6e5', 33, 'Warehouse A', 'Red', 'L2', 'A12', 'B3', 'Bin45', 'North', '2025-02-12 10:18:56', 1, NULL, NULL, 1, 1);

SELECT setval('locations_id_seq', (SELECT MAX(id) FROM locations));

-- ============================================================
-- Table: modules
-- ============================================================
CREATE TABLE modules (
                         id SERIAL PRIMARY KEY,
                         sys_code VARCHAR(255),
                         module_type_id INTEGER,
                         name VARCHAR(255),
                         ordering INTEGER,
                         status SMALLINT DEFAULT 1,
                         type SMALLINT,
                         price DECIMAL(15,3),
                         description TEXT
);

CREATE INDEX idx_modules_module_type ON modules(module_type_id);

INSERT INTO modules (id, sys_code, module_type_id, name, ordering, status, type, price, description) VALUES
                                                                                                         (1, 'ecbf7b0b55c01774415c6028002a801d', 1, 'Home Page (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (2, 'ce4ec441a886f7b17ae6c2c765b7d537', 2, 'User (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (6, 'dab1e688c169a92c40db4ba93564c601', 3, 'Group (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (10, 'e15c044b89daf222176327ed34c54c3a', 4, 'System Role (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (11, 'aabc17e90898fb8f51dbd85ea36ff710', 4, 'System Role (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (12, '04c82aeb5c4c4f0d5b1fc9f99ebf2ca0', 4, 'System Role (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (13, '440588775bc55dd0a8964c3236360c1b', 4, 'System Role (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (14, '8f9f63af900f219336e944c32c110b64', 5, 'Department (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (15, '63e0f44cfd878d5f4fd28dd805c4d3d6', 5, 'Department (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (16, '107b8799c7d83b769b0ee033cf198b92', 5, 'Department (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (17, '0f99f0d432c9cc77164f3f669eb8cecb', 5, 'Department (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (18, 'ecd71e9342c04ee6a03bd6b63f87071d', 6, 'Branch (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (19, '76a347b9789508940529bbdeee6caf19', 6, 'Branch (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (20, '8101df8eb736d1831a5674b3ca85d10c', 6, 'Branch (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (21, 'a1ae4eae873519b3d05cd413d69f8ebc', 6, 'Branch (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (26, 'addda134ecb17a631d792a7b20933ee5', 7, 'Major (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (27, '2508852fae1a09ef50273ab30ca0ca3d', 7, 'Major (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (28, 'df3d2c9a62cc745bb8e79b48be629a17', 7, 'Major (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (29, '220d2bc01df3bfebbb7ea08866b30cc4', 7, 'Major (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (109, 'e5d00f82f48ee6ce10f047f8e1ed1117', 26, 'Exchange Rate (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (110, '0ef778d9144c34d3d2b1e709a166093b', 26, 'Exchange Rate (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (121, 'a4d90f50cd762336a9864a6de6c7217b', 29, 'Discount (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (122, 'd38c4d61c8650ceaf057b3bbdb50837f', 29, 'Discount (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (123, '84dfbdc956ba9045c405b6b237f554b8', 29, 'Discount (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (124, '9edccda47b55d3e8eb09eea7e65207b2', 29, 'Discount (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (237, 'f47a3889191647893727eb52ab226fb9', 28, 'Report (Deposit)', 807, 0, NULL, NULL, NULL),
                                                                                                         (247, '3bb70778e31901e01819e83afb03418a', 46, 'Student (view)', 1, 1, NULL, NULL, NULL),
                                                                                                         (248, 'd68602d3e98cb8be8954e06f51d8a131', 46, 'Student (add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (249, '92fb1671051de0906f0512ffaa367f98', 46, 'Student (edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (250, 'bfac0fea89c977c74bbf50998fa3bad2', 46, 'Student (delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (432, '9e6fe9172f71200d9219775f108a9748', 2, 'User (Add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (433, 'd391f7120097ed705e9d1b6a239ce50d', 2, 'User (Edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (434, '758f7e09f47bdc63b7690119ca9d97f5', 2, 'User (Delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (484, '8722316b992b37718581a3b19c527f64', 2, 'User (Edit Profile)', 5, 1, NULL, NULL, NULL),
                                                                                                         (506, '77184a0631c7eb1bc42f79855e5b974e', 28, 'Report (Quotation)', 317, 0, NULL, NULL, NULL),
                                                                                                         (507, '1aef08e19965750f7dfcd56d8ffb8661', 28, 'Report (Quotation)', 1, 0, NULL, NULL, NULL),
                                                                                                         (508, 'eefa9c9a38144100f4e7243387c878f4', 28, 'Report (Sales Order)', 2, 0, NULL, NULL, NULL),
                                                                                                         (509, '3581f3537c26638540fb0209b69481cc', 80, 'Products Reorder Level', 3, 1, NULL, NULL, NULL),
                                                                                                         (510, '9ed0a750cd0df4a621b26cfab41f54b5', 80, 'Products Expire Date', 3, 1, NULL, NULL, NULL),
                                                                                                         (511, '8a3117b25b3bf2d85f8236b4a29255c2', 89, 'Branch (View)', 1, 1, NULL, NULL, NULL),
                                                                                                         (512, '602a2e7484fa9c294c8f4d2a09529d45', 89, 'Branch (Add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (513, 'dd8efad2147f85d5d4cc79131040129f', 89, 'Branch (Edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (514, '6da9cf5a41d969d09aa4d306cb48a14d', 89, 'Branch (Delete)', 4, 1, NULL, NULL, NULL),
                                                                                                         (515, 'fd9bb8bebb941f02fa67ef98eb61e197', 90, 'Branch Type (View)', 1, 1, NULL, NULL, NULL),
                                                                                                         (516, 'cd2ce23ee207229a3b884e6741859e7f', 90, 'Branch Type (Add)', 2, 1, NULL, NULL, NULL),
                                                                                                         (517, '30e6f643de3ee039e0f3c5f3738cedec', 90, 'Branch Type (Edit)', 3, 1, NULL, NULL, NULL),
                                                                                                         (518, '8c10226fb0d72f8299b0513db56a024e', 90, 'Branch Type (Delete)', 4, 1, NULL, NULL, NULL);

SELECT setval('modules_id_seq', (SELECT MAX(id) FROM modules));

-- ============================================================
-- Table: module_types
-- ============================================================
CREATE TABLE module_types (
                              id SERIAL PRIMARY KEY,
                              sys_code VARCHAR(255),
                              name VARCHAR(255),
                              ordering INTEGER,
                              status SMALLINT DEFAULT 1
);

INSERT INTO module_types (id, sys_code, name, ordering, status) VALUES
                                                                    (1, 'cb996c86207a08b1a599e7b6f125414f', 'Home Page', 101, 1),
                                                                    (2, 'a23612854862345fc7838cd39ac0a3e6', 'User', 102, 1),
                                                                    (3, '359cc158d7a49a2b7f40e7ff4eb58445', 'Group', 103, 1),
                                                                    (4, 'da7e27dcfbcd411e50df94df1a81087d', 'System Role', 107, 1),
                                                                    (5, '9a0406de6a08f981ff727d2f3cf2f38f', 'Department', 106, 1),
                                                                    (6, '93c965fefc3afd248e6887020c597111', 'Branch', 108, 1),
                                                                    (7, '6034114007bca67be50b8831234d7479', 'Major', 109, 1),
                                                                    (8, '4dd3111c4a451c2f48ac9eb403112347', 'Province', 618, 0),
                                                                    (9, 'c65a68ed3df60658cb3f828baf301b3a', 'District', 619, 0),
                                                                    (10, '44ec89f432ce5b6a6457858310c99e21', 'Commune', 620, 0),
                                                                    (11, '72974a1cfcafcf280611b10a82e74b54', 'Village', 621, 0),
                                                                    (26, 'dfaf70bedd335c7b50d1fcea59d005aa', 'Exchange Rate', 131, 1),
                                                                    (28, '3a7b8aef75c850fc0c88329a2fe85317', 'Report', 156, 1),
                                                                    (29, '4ffce8932839606f89751f42736355d7', 'Discount', 121, 0),
                                                                    (46, 'd1e26553adb9b5b5e9314d79f1471026', 'Student', 615, 1),
                                                                    (89, '0f8e73d6ea007c2b9be02e66b16cf053', 'Branch', 123, 1),
                                                                    (108, 'c0d20505cb718b43bff662376748b55c', 'Dashboard', 157, 1);

SELECT setval('module_types_id_seq', (SELECT MAX(id) FROM module_types));

-- ============================================================
-- Table: oauth_client_details
-- ============================================================
CREATE TABLE oauth_client_details (
                                      client_id VARCHAR(255) PRIMARY KEY,
                                      resource_ids VARCHAR(255),
                                      client_secret VARCHAR(255),
                                      scope VARCHAR(255),
                                      authorized_grant_types VARCHAR(255),
                                      web_server_redirect_uri VARCHAR(255),
                                      authorities VARCHAR(255),
                                      access_token_validity INTEGER,
                                      refresh_token_validity INTEGER,
                                      additional_information VARCHAR(4096),
                                      autoapprove VARCHAR(255),
                                      device_info VARCHAR(255),
                                      created TIMESTAMP
);

CREATE UNIQUE INDEX idx_oauth_client_unique ON oauth_client_details(client_id, client_secret);

INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove, device_info, created) VALUES
                                                                                                                                                                                                                                                             ('1782309494043-i8fvhz40b', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 20:58:19'),
                                                                                                                                                                                                                                                             ('1782309519032-y998yitx9', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 20:58:39'),
                                                                                                                                                                                                                                                             ('1782309527162-xbajlg5cr', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 20:58:47'),
                                                                                                                                                                                                                                                             ('1782309541840-sk3g989uz', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 20:59:02'),
                                                                                                                                                                                                                                                             ('1782309714244-ja7jmu3yl', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:01:54'),
                                                                                                                                                                                                                                                             ('1782309816988-4s0ald27y', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:03:38'),
                                                                                                                                                                                                                                                             ('1782310096072-wjjmkg36d', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:08:16'),
                                                                                                                                                                                                                                                             ('1782310122358-22w8i6z46', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:08:42'),
                                                                                                                                                                                                                                                             ('1782310224070-0uxk18b57', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:10:25'),
                                                                                                                                                                                                                                                             ('1782310284402-85tx2dc3s', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:11:24'),
                                                                                                                                                                                                                                                             ('1782310297220-bvlqft8lx', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:11:37'),
                                                                                                                                                                                                                                                             ('1782310300584-uj1humces', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:11:40'),
                                                                                                                                                                                                                                                             ('1782310332083-wcub1zt7n', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:12:12'),
                                                                                                                                                                                                                                                             ('1782310584218-tmc6slbp3', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Chrome on Windows', '2026-06-24 21:16:25'),
                                                                                                                                                                                                                                                             ('string', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'string', '2026-06-24 21:18:59'),
                                                                                                                                                                                                                                                             ('web-app', NULL, '#SDRGS9873984DT', 'read,write,trust', 'password,refresh_token', NULL, NULL, 2592000, 7776000, NULL, '1', 'Web Application', '2026-04-05 14:24:54');

-- ============================================================
-- Table: payment
-- ============================================================
CREATE TABLE payment (
                         id BIGSERIAL PRIMARY KEY,
                         reg_id BIGINT NOT NULL,
                         amount DECIMAL(12,2) NOT NULL,
                         discount INTEGER,
                         method_id BIGINT NOT NULL,
                         status VARCHAR(20) DEFAULT 'pending',
                         paid_at TIMESTAMP,
                         reference_no VARCHAR(100)
);

-- ============================================================
-- Table: payment_methods
-- ============================================================
CREATE TABLE payment_methods (
                                 id SERIAL PRIMARY KEY,
                                 sys_code VARCHAR(50),
                                 name VARCHAR(50),
                                 photo VARCHAR(50),
                                 discount_percent DECIMAL(7,3),
                                 service_fee DECIMAL(7,3),
                                 created TIMESTAMP,
                                 created_by INTEGER,
                                 modified TIMESTAMP,
                                 modified_by INTEGER,
                                 is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_payment_methods_name ON payment_methods(name);
CREATE INDEX idx_payment_methods_sys_code ON payment_methods(sys_code);

INSERT INTO payment_methods (id, sys_code, name, photo, discount_percent, service_fee, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                                                                  (1, '1', 'Cash', NULL, 0.000, 0.000, '2025-08-30 14:07:29', 1, '2025-08-30 14:07:32', NULL, 1),
                                                                                                                                                  (2, '2', 'Wing', NULL, 0.000, 0.000, '2025-08-30 11:35:29', 1, '2025-08-30 11:35:29', NULL, 1),
                                                                                                                                                  (3, '3', 'ABA KHQR', NULL, 0.000, 0.000, '2025-08-30 11:35:29', 1, '2025-08-30 11:35:29', NULL, 1),
                                                                                                                                                  (4, '4', 'Credit/Debit Card', NULL, 0.000, 0.000, '2025-08-30 11:35:29', 1, '2025-08-30 11:35:29', NULL, 1),
                                                                                                                                                  (5, '5', 'AliPay', NULL, 0.000, 0.000, '2025-08-30 11:35:29', 1, '2025-08-30 11:35:29', NULL, 1),
                                                                                                                                                  (6, '6', 'Acleda', NULL, 0.000, 0.000, '2025-08-30 11:35:29', 1, '2025-08-30 11:35:29', NULL, 1);

SELECT setval('payment_methods_id_seq', (SELECT MAX(id) FROM payment_methods));

-- ============================================================
-- Table: permissions
-- ============================================================
CREATE TABLE permissions (
                             id BIGSERIAL PRIMARY KEY,
                             group_id INTEGER,
                             module_id INTEGER
);

CREATE INDEX idx_permissions_group_module ON permissions(group_id, module_id);

INSERT INTO permissions (id, group_id, module_id) VALUES
                                                      (33373, 1, 1),
                                                      (33374, 1, 2),
                                                      (33377, 1, 6),
                                                      (33382, 1, 10),
                                                      (33383, 1, 11),
                                                      (33384, 1, 12),
                                                      (33385, 1, 13),
                                                      (33378, 1, 14),
                                                      (33379, 1, 15),
                                                      (33380, 1, 16),
                                                      (33381, 1, 17),
                                                      (33386, 1, 18),
                                                      (33387, 1, 19),
                                                      (33388, 1, 20),
                                                      (33389, 1, 26),
                                                      (33390, 1, 27),
                                                      (33391, 1, 28),
                                                      (33392, 1, 29),
                                                      (33400, 1, 247),
                                                      (33401, 1, 248),
                                                      (33402, 1, 249),
                                                      (33403, 1, 250),
                                                      (33398, 1, 301),
                                                      (33399, 1, 302),
                                                      (33393, 1, 344),
                                                      (33394, 1, 345),
                                                      (33395, 1, 350),
                                                      (33396, 1, 351),
                                                      (33397, 1, 352),
                                                      (33375, 1, 432),
                                                      (33376, 1, 433),
                                                      (32398, 2, 1),
                                                      (32400, 2, 2),
                                                      (32399, 2, 10),
                                                      (32401, 2, 12),
                                                      (32402, 2, 13),
                                                      (30874, 2, 432),
                                                      (33173, 4, 1),
                                                      (33156, 12, 1),
                                                      (33157, 12, 2),
                                                      (33163, 13, 18),
                                                      (33164, 13, 19),
                                                      (33165, 13, 20),
                                                      (33166, 13, 21),
                                                      (33167, 13, 297),
                                                      (33364, 23, 1),
                                                      (33369, 24, 2),
                                                      (33370, 24, 26),
                                                      (33371, 24, 109),
                                                      (33372, 24, 110);

SELECT setval('permissions_id_seq', (SELECT MAX(id) FROM permissions));

-- ============================================================
-- Table: pgroup_companies
-- ============================================================
CREATE TABLE pgroup_companies (
                                  id SERIAL PRIMARY KEY,
                                  pgroup_id INTEGER,
                                  company_id INTEGER
);

CREATE INDEX idx_pgroup_companies_pgroup ON pgroup_companies(pgroup_id);
CREATE INDEX idx_pgroup_companies_company ON pgroup_companies(company_id);

INSERT INTO pgroup_companies (id, pgroup_id, company_id) VALUES
                                                             (1, 1, 1),
                                                             (2, 2, 1),
                                                             (3, 3, 1),
                                                             (5, 5, 1),
                                                             (7, 7, 1),
                                                             (8, 8, 1),
                                                             (10, 10, 1),
                                                             (11, 11, 1),
                                                             (12, 12, 1),
                                                             (13, 13, 1),
                                                             (14, 14, 1),
                                                             (15, 15, 1),
                                                             (16, 16, 1),
                                                             (17, 17, 1),
                                                             (18, 18, 1),
                                                             (19, 19, 1),
                                                             (20, 20, 1),
                                                             (21, 21, 1),
                                                             (23, 23, 1),
                                                             (24, 24, 1),
                                                             (25, 25, 1),
                                                             (26, 26, 1),
                                                             (27, 27, 1),
                                                             (28, 28, 1),
                                                             (29, 29, 1),
                                                             (30, 30, 1),
                                                             (31, 31, 1),
                                                             (32, 32, 1),
                                                             (33, 33, 1),
                                                             (34, 34, 1),
                                                             (35, 35, 1),
                                                             (36, 36, 1),
                                                             (37, 37, 1),
                                                             (38, 38, 1),
                                                             (39, 39, 1),
                                                             (40, 40, 1),
                                                             (41, 41, 1),
                                                             (42, 42, 1),
                                                             (43, 43, 1),
                                                             (44, 44, 1),
                                                             (45, 45, 1),
                                                             (46, 46, 1),
                                                             (47, 47, 1),
                                                             (48, 48, 1),
                                                             (49, 49, 1),
                                                             (50, 50, 1),
                                                             (51, 51, 1),
                                                             (52, 52, 1),
                                                             (53, 53, 1),
                                                             (54, 54, 1),
                                                             (55, 55, 1),
                                                             (56, 56, 1),
                                                             (57, 57, 1),
                                                             (58, 58, 1),
                                                             (59, 59, 1),
                                                             (60, 60, 1),
                                                             (61, 61, 1),
                                                             (62, 62, 1),
                                                             (63, 4, 1),
                                                             (64, 6, 1),
                                                             (65, 9, 1),
                                                             (66, 22, 1),
                                                             (67, 63, 1),
                                                             (68, 64, 1),
                                                             (69, 65, 1);

SELECT setval('pgroup_companies_id_seq', (SELECT MAX(id) FROM pgroup_companies));

-- ============================================================
-- Table: positions
-- ============================================================
CREATE TABLE positions (
                           id SERIAL PRIMARY KEY,
                           sys_code VARCHAR(50),
                           name VARCHAR(255),
                           created TIMESTAMP,
                           created_by BIGINT,
                           modified TIMESTAMP,
                           modified_by BIGINT,
                           is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_positions_search ON positions(name, is_active);
CREATE INDEX idx_positions_sys_code ON positions(sys_code);

INSERT INTO positions (id, sys_code, name, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                      (1, '46dcb46efc390be2abae00339beb846f', 'General Director ', '2020-04-03 12:10:09', 1, '2020-04-03 12:10:18', 1, 1),
                                                                                                      (2, 'cd0724d04e36ca36314799f2caa0cad4', 'Deputy Director ', '2020-04-03 12:10:47', 1, '2020-04-03 12:10:47', NULL, 1),
                                                                                                      (3, '934f6f80ebf519cc1223276c6bf6e8b1', 'Doctor specialist ', '2020-04-03 12:11:23', 1, '2020-04-03 12:11:23', NULL, 1),
                                                                                                      (4, '202acebeaed2c89cff9292313e436232', 'Professor ', '2020-04-03 12:11:46', 1, '2020-04-03 12:11:46', NULL, 1),
                                                                                                      (5, '76aee42f008d94bd1166efa66400c92e', 'Cleaner ', '2020-04-03 12:11:55', 1, '2020-04-03 12:11:55', NULL, 1),
                                                                                                      (6, '85dab19757b9896c3003149038b7a961', 'Security ', '2020-04-03 12:12:03', 1, '2020-04-03 12:12:13', 1, 1),
                                                                                                      (7, 'dd2fefd578dd9cd58373fa3303c1cf9e', 'Administration ', '2020-04-03 12:12:37', 1, '2020-04-03 12:12:37', NULL, 1),
                                                                                                      (8, '0abe8c4dca95617ef801e1e88ae9161f', 'Accountant', '2020-04-03 12:12:49', 1, '2020-04-03 12:12:49', NULL, 1),
                                                                                                      (9, '33fab3a801ea9e7aeec4bc3068a0e2e8', 'Nurse ', '2020-04-03 12:13:01', 1, '2020-04-03 12:13:01', NULL, 1),
                                                                                                      (10, '4a9f57d966ae9d362288130d7ea61a2b', 'Receptionist ', '2020-04-03 12:13:14', 1, '2020-04-03 12:13:14', NULL, 1),
                                                                                                      (12, '7789d23c-cfc9-11ef-8dd4-6c2b5979c6e5', 'IT', '2025-01-11 10:09:27', 1, '2025-01-11 10:09:55', NULL, 2),
                                                                                                      (13, '884c153d-cfc9-11ef-8dd4-6c2b5979c6e5', 'ITE', '2025-01-11 10:09:55', 1, '2025-01-11 10:09:55', 1, 1);

SELECT setval('positions_id_seq', (SELECT MAX(id) FROM positions));

-- ============================================================
-- Table: programs
-- ============================================================
CREATE TABLE programs (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          name VARCHAR(200) NOT NULL,
                          duration_years INTEGER NOT NULL,
                          tuition_fee DECIMAL(12,2) NOT NULL
);

INSERT INTO programs (id, code, name, duration_years, tuition_fee) VALUES
                                                                       (1, 'BIT', 'Bachelor of Information Technology', 4, 500.00),
                                                                       (2, 'BSE', 'Bachelor of Software Engineering', 4, 1500.00),
                                                                       (3, 'BBA', 'Bachelor of Business Administration', 4, 1000.00);

SELECT setval('programs_id_seq', (SELECT MAX(id) FROM programs));

-- ============================================================
-- Table: provinces
-- ============================================================
CREATE TABLE provinces (
                           id SERIAL PRIMARY KEY,
                           sys_code VARCHAR(50),
                           name VARCHAR(255) UNIQUE,
                           abbr VARCHAR(50),
                           created TIMESTAMP,
                           created_by BIGINT,
                           modified TIMESTAMP,
                           modified_by BIGINT,
                           is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_provinces_sys_code ON provinces(sys_code);
CREATE INDEX idx_provinces_search ON provinces(name, is_active);

INSERT INTO provinces (id, sys_code, name, abbr, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                            (26, 'f1b57977c0471a4ebf078a28a7b79eaf', 'PHNOM PENH', 'PNH', '2014-12-03 11:27:44', 1, '2014-12-03 11:27:44', NULL, 1),
                                                                                                            (27, '2cfaa750990b70acb718e9a7ea28e4be', 'BANTEAY MEANCHEY', 'BMC', '2014-12-03 11:27:58', 1, '2014-12-03 11:27:58', NULL, 1),
                                                                                                            (28, '474c5eaacd210a5d5024f9a47b15da2f', 'BATTAMBANG', 'BDB', '2014-12-03 11:28:09', 1, '2014-12-03 11:28:09', NULL, 1),
                                                                                                            (29, '047fca4a002d628e934b814282c11d2f', 'KAMPONG CHAM', 'KPC', '2014-12-03 11:28:39', 1, '2014-12-03 11:28:39', NULL, 1),
                                                                                                            (30, '3bc73a7b2937898ee2a40d89fd21f939', 'KAMPONG CHHNANG', 'KCH', '2014-12-03 11:28:51', 1, '2014-12-03 11:28:51', NULL, 1),
                                                                                                            (31, '06fa61d3f49302ad9cb59405efd4890c', 'KAMPONG SPEU', 'KPS', '2014-12-03 11:29:08', 1, '2014-12-03 11:29:08', NULL, 1),
                                                                                                            (32, 'faee449b5e53c3553ea6275e848b1c13', 'KAMPONG THOM', 'KPT', '2014-12-03 11:29:19', 1, '2014-12-03 11:29:19', NULL, 1),
                                                                                                            (33, 'bf11ce1a07ee94c363f47b22d9af86af', 'KAMPOT', 'KAP', '2014-12-03 11:29:39', 1, '2014-12-03 11:29:39', NULL, 1),
                                                                                                            (34, '8d0dda52f7b41751d1bc407548504c69', 'KANDAL', 'KND', '2014-12-03 11:29:51', 1, '2014-12-03 11:29:51', NULL, 1),
                                                                                                            (35, 'ba7fe9da8d7baa44eed4dd9456b894e1', 'KEP', 'KEP', '2014-12-03 11:30:02', 1, '2014-12-03 11:30:02', NULL, 1),
                                                                                                            (36, 'f62b6c848a68479c8538f6ae11fb6579', 'KRATIE', 'KRT', '2014-12-03 11:30:12', 1, '2014-12-03 11:30:12', NULL, 1),
                                                                                                            (37, 'cd34420e5e22c5f3ed08fc7c5ed76c1d', 'MONDULKIRI', 'MKR', '2014-12-03 11:30:22', 1, '2014-12-03 11:30:22', NULL, 1),
                                                                                                            (38, '251e199f12ff121c941df156978b54e7', 'ODDAR MEANCHEY', 'OMC', '2014-12-03 11:30:35', 1, '2014-12-03 11:30:35', NULL, 1),
                                                                                                            (39, '87e6d5327a2c2814de4cdca259b89ad8', 'PAILIN', 'PAL', '2014-12-03 11:30:45', 1, '2014-12-03 11:30:45', NULL, 1),
                                                                                                            (40, '69b6a0a2da0bd20859fd864f933c6705', 'PREAH SIHANOUK', 'SHV', '2014-12-03 11:30:55', 1, '2014-12-03 11:30:55', NULL, 1),
                                                                                                            (41, '73f129ac5009cff04d42e1aa01f525b7', 'PREAH VIHEAR', 'PVH', '2014-12-03 11:31:10', 1, '2014-12-03 11:31:10', NULL, 1),
                                                                                                            (42, '42a446e138f4529811e55c4c84571c86', 'PURSAT', 'PUR', '2014-12-03 11:31:23', 1, '2014-12-03 11:31:23', NULL, 1),
                                                                                                            (43, 'a9f666fe8e2e715c7df754ca6447307e', 'PREY VENG', 'PRV', '2014-12-03 11:31:44', 1, '2014-12-03 11:31:44', NULL, 1),
                                                                                                            (44, '081e97d0ddca6298ae77a2075028e12a', 'RATANAKIRI', 'RKR', '2014-12-03 11:31:58', 1, '2014-12-03 11:31:58', NULL, 1),
                                                                                                            (45, '51ad012d8a57bed387a488fcd8fb593d', 'SIEM REAP', 'SMR', '2014-12-03 11:32:11', 1, '2014-12-03 11:32:11', NULL, 1),
                                                                                                            (46, 'a6e784004da8c809d3396e8b1ee26885', 'STUNG TRENG', 'STG', '2014-12-03 11:32:19', 1, '2014-12-03 11:32:19', NULL, 1),
                                                                                                            (47, 'b68265acacd680610e6191c36821a1aa', 'SVAY RIENG', 'SVR', '2014-12-03 11:32:30', 1, '2014-12-03 11:32:30', NULL, 1),
                                                                                                            (48, 'f32b1fea1b7b21939c070ff181ee6a00', 'TAKEO', 'TKE', '2014-12-03 11:32:40', 1, '2014-12-03 11:32:40', NULL, 1),
                                                                                                            (49, '760148e8d94332593ac6d0cde9ad6a1c', 'TBONG KHMUM', 'TBK', '2014-12-03 11:32:51', 1, '2014-12-03 11:32:51', NULL, 1),
                                                                                                            (50, 'a3a4ef3d55b3071c80ccf1f3b62c197c', 'KOH KONG', 'KOK', '2014-12-03 11:32:59', 1, '2014-12-03 11:32:59', NULL, 1);

SELECT setval('provinces_id_seq', (SELECT MAX(id) FROM provinces));

-- ============================================================
-- Table: receipts (sample data - due to size, only structure shown)
-- ============================================================
CREATE TABLE receipts (
                          id BIGSERIAL PRIMARY KEY,
                          invoice_id BIGINT,
                          chart_account_id BIGINT,
                          receipt_code VARCHAR(255),
                          exchange_rate_id BIGINT,
                          total_amount_paid DOUBLE PRECISION DEFAULT 0,
                          balance DOUBLE PRECISION DEFAULT 0,
                          total_dis DOUBLE PRECISION DEFAULT 0,
                          total_dis_p DOUBLE PRECISION DEFAULT 0,
                          pay_date DATE,
                          due_date FLOAT,
                          created TIMESTAMP,
                          created_by INTEGER,
                          modified TIMESTAMP,
                          modified_by INTEGER,
                          is_void SMALLINT DEFAULT 0
);

CREATE INDEX idx_receipts_receipt_code ON receipts(receipt_code);

-- ============================================================
-- Table: receipts_deposit
-- ============================================================
CREATE TABLE receipts_deposit (
                                  id SERIAL PRIMARY KEY,
                                  receipt_id BIGINT,
                                  date DATE,
                                  deposit_id BIGINT,
                                  patient_id INTEGER,
                                  deposit_amount DOUBLE PRECISION
);

-- ============================================================
-- Table: receive_payment_details
-- ============================================================
CREATE TABLE receive_payment_details (
                                         id BIGSERIAL PRIMARY KEY,
                                         receive_payment_id BIGINT,
                                         sales_order_id BIGINT,
                                         amount_due DECIMAL(15,3) DEFAULT 0.000,
                                         paid DECIMAL(15,3) DEFAULT 0.000,
                                         paid_other DECIMAL(15,3) DEFAULT 0.000,
                                         discount DECIMAL(15,3) DEFAULT 0.000,
                                         discount_other DECIMAL(15,3) DEFAULT 0.000,
                                         balance DECIMAL(15,3) DEFAULT 0.000,
                                         due_date DATE
);

-- ============================================================
-- Table: refund_history
-- ============================================================
CREATE TABLE refund_history (
                                id BIGSERIAL PRIMARY KEY,
                                student_id BIGINT NOT NULL,
                                balance_id BIGINT NOT NULL,
                                refund_amount DOUBLE PRECISION NOT NULL,
                                refunded_by BIGINT NOT NULL,
                                refund_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                note TEXT
);

CREATE INDEX idx_refund_history_student ON refund_history(student_id);
CREATE INDEX idx_refund_history_balance ON refund_history(balance_id);

-- ============================================================
-- Table: refun_deposits
-- ============================================================
CREATE TABLE refun_deposits (
                                id SERIAL PRIMARY KEY,
                                refund_code VARCHAR(50) NOT NULL,
                                date DATE,
                                patient_id INTEGER,
                                amount DOUBLE PRECISION,
                                created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                created_by BIGINT
);

-- ============================================================
-- Table: registration
-- ============================================================
CREATE TABLE registration (
                              id BIGSERIAL PRIMARY KEY,
                              student_id BIGINT NOT NULL,
                              program_id BIGINT NOT NULL,
                              semester VARCHAR(50) NOT NULL,
                              academic_year VARCHAR(20) NOT NULL,
                              status VARCHAR(20) DEFAULT 'pending',
                              registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- Table: registration_course
-- ============================================================
CREATE TABLE registration_course (
                                     id BIGSERIAL PRIMARY KEY,
                                     reg_id BIGINT NOT NULL,
                                     course_id BIGINT NOT NULL
);

-- ============================================================
-- Table: shifts
-- ============================================================
CREATE TABLE shifts (
                        id SERIAL PRIMARY KEY,
                        company_id INTEGER,
                        branch_id INTEGER,
                        shift_collect_id INTEGER,
                        exchange_rate_id INTEGER,
                        shift_code VARCHAR(50),
                        date_start TIMESTAMP,
                        date_end TIMESTAMP,
                        total_register DECIMAL(15,3),
                        total_register_other DECIMAL(15,3),
                        total_sales DECIMAL(15,3),
                        total_sales_other DECIMAL(15,3),
                        total_acture DECIMAL(15,3) DEFAULT 0.000,
                        total_acture_other DECIMAL(15,3) DEFAULT 0.000,
                        total_spread DECIMAL(15,3) DEFAULT 0.000,
                        register_memo TEXT,
                        close_shift_memo TEXT,
                        created TIMESTAMP,
                        created_by INTEGER,
                        status SMALLINT DEFAULT 1
);

CREATE INDEX idx_shifts_times ON shifts(date_start, date_end);
CREATE INDEX idx_shifts_filters ON shifts(created_by, status, shift_collect_id);
CREATE INDEX idx_shifts_company ON shifts(company_id, branch_id);

-- ============================================================
-- Table: students
-- ============================================================
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          code VARCHAR(50) NOT NULL UNIQUE,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          dob DATE,
                          gender VARCHAR(10),
                          phone VARCHAR(20),
                          email VARCHAR(100),
                          address VARCHAR(255),
                          created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by VARCHAR(20),
                          modified TIMESTAMP,
                          modified_by BIGINT,
                          status BIGINT
);

INSERT INTO students (id, code, first_name, last_name, dob, gender, phone, email, address, created, created_by, modified, modified_by, status) VALUES
                                                                                                                                                   (33, '20250002', 'Kim', 'Sok', '2025-09-29', 'male', '010555777', 'testuser@gmail.com', 'ss', '2025-09-05 15:00:46', '1', NULL, NULL, 1),
                                                                                                                                                   (32, '20250001', 'ss', 'ss', '2025-09-29', 'male', '010', 'ximojim221@claspira.com', 'sss', '2025-09-05 11:17:13', '1', NULL, NULL, 1),
                                                                                                                                                   (34, '20250003', 's', 's', '2025-09-29', 'male', '010', 'ximojim221@claspira.com', 'sss', '2025-09-05 16:11:19', '1', NULL, NULL, 1),
                                                                                                                                                   (35, '20250004', 'ss', 'ss', '2025-09-29', 'male', '010', 'testuser@gmail.com', 'sss', '2025-09-09 14:50:22', '1', NULL, NULL, 3),
                                                                                                                                                   (36, '20250005', 'sdf', 'sdf', '2025-09-29', 'male', '010', 'ximojim221@claspira.com', 'sdf', '2025-09-17 20:40:19', '1', NULL, NULL, 2),
                                                                                                                                                   (38, '20260001', 'tetsssf', 'asdsd', NULL, 'male', '0125535434', 'safdfsf@gmil.com', '4244242', '2026-01-17 09:51:39', '1', NULL, NULL, 2),
                                                                                                                                                   (39, '20260002', 'sok', 'lifft', NULL, 'male', '012555444', 'sdsd@gmail.com', '024242', '2026-01-18 08:44:47', '1', NULL, NULL, 2),
                                                                                                                                                   (40, '20260003', 'sad', 'asdsa', NULL, 'male', '2123', 'gmail.com', '123123', '2026-01-18 08:47:47', '1', NULL, NULL, 2),
                                                                                                                                                   (41, '20260004', 'sadas', 'asd', NULL, 'male', '01245245', 'ssdvgrg@gmail.com', 'adadas', '2026-01-18 08:53:44', '1', NULL, NULL, 2),
                                                                                                                                                   (42, '20260005', 'sok', 'ya', NULL, 'male', '012', 'dd@gmail.com', 'fcsdf', '2026-01-18 09:25:12', '1', NULL, NULL, 3),
                                                                                                                                                   (43, '20260006', 'sda', 'asd', NULL, 'male', '012', 'fszd@gmail.com', 'fssds', '2026-01-18 09:45:45', '1', NULL, NULL, 2),
                                                                                                                                                   (44, '20260007', 'sfsef', 'adad', NULL, 'male', '012', 'sfsd@gmail.com', '2.1', '2026-01-18 10:01:27', '1', NULL, NULL, 2),
                                                                                                                                                   (45, '20260008', 'dsfsf', 'dsafasf', NULL, 'male', '01257545', 'fssf@gmail.com', 'fasdf', '2026-01-18 10:05:06', '1', NULL, NULL, 2),
                                                                                                                                                   (46, '20260009', 'sdf', 'dsfa', NULL, 'male', '012', 'sdf@gmail.com', '21', '2026-01-18 10:12:35', '1', NULL, NULL, 2),
                                                                                                                                                   (47, '20260010', 'asds', 'asd', NULL, 'male', '012', 'sdf@gmail.com', 'asasd', '2026-01-18 10:17:44', '1', NULL, NULL, 3),
                                                                                                                                                   (48, '20260011', 'kim', 'ya', NULL, 'male', '0124747', 'kimya@gmail.com', 'sfsdf', '2026-01-21 09:57:38', '1', NULL, NULL, 2),
                                                                                                                                                   (49, '20260012', 'sok', 'heng', NULL, 'male', '086779999', 'sokkimheng@gmail.com', 'sdfsf', '2026-01-21 10:04:19', '1', NULL, NULL, 2),
                                                                                                                                                   (50, '20260013', 'nangsdf', 'sfsf', NULL, 'male', '012454441', 'nangfsdf@gmail.com', 'asdad', '2026-01-21 10:55:27', '1', NULL, NULL, 2),
                                                                                                                                                   (51, '20260014', 'ASP', 'NET', NULL, 'male', '012754414', 'adasda@gmail.com', 'sdfdsf', '2026-01-21 11:10:51', '1', NULL, NULL, 2),
                                                                                                                                                   (52, '20260015', 'sad', 'fadsf', NULL, 'male', '012', 'asdas@gmail.com', 'add', '2026-01-21 11:19:33', '1', NULL, NULL, 2),
                                                                                                                                                   (53, '20260016', 'aqza', 'aqza', NULL, 'male', '012', 'zvczcxv@gmailc.om', 'add', '2026-01-21 11:28:13', '1', NULL, NULL, 2);

SELECT setval('students_id_seq', (SELECT MAX(id) FROM students));

-- ============================================================
-- Table: term_conditions
-- ============================================================
CREATE TABLE term_conditions (
                                 id SERIAL PRIMARY KEY,
                                 sys_code VARCHAR(50),
                                 term_condition_type_id INTEGER,
                                 name VARCHAR(500),
                                 description TEXT,
                                 created TIMESTAMP,
                                 created_by BIGINT,
                                 modified TIMESTAMP,
                                 modified_by BIGINT,
                                 is_active SMALLINT DEFAULT 1
);

CREATE INDEX idx_term_conditions_type ON term_conditions(term_condition_type_id);
CREATE INDEX idx_term_conditions_sys_code ON term_conditions(sys_code);
CREATE INDEX idx_term_conditions_search ON term_conditions(name, is_active);

-- ============================================================
-- Table: transactions
-- ============================================================
CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              tran_id VARCHAR(50) NOT NULL UNIQUE,
                              student_id BIGINT NOT NULL,
                              invoice_no VARCHAR(255),
                              total DECIMAL(15,2),
                              deposit DECIMAL(15,2),
                              balance_due DECIMAL(15,2),
                              status SMALLINT NOT NULL DEFAULT 0,
                              created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              created_by BIGINT
);

INSERT INTO transactions (id, tran_id, student_id, invoice_no, total, deposit, balance_due, status, created, created_by) VALUES
                                                                                                                             (1, 'STDHEHOVQSRSU', 32, '20250905001', 500.00, 0.00, 500.00, 1, '2025-09-05 04:17:13', NULL),
                                                                                                                             (2, 'STDERTT7KQUWO', 33, '20250905001', 500.00, 0.00, 500.00, 1, '2025-09-05 08:00:46', NULL),
                                                                                                                             (3, 'STDGFSBEU9JSF', 34, '20250905001', 500.00, 0.00, 500.00, 1, '2025-09-05 09:11:19', NULL),
                                                                                                                             (4, 'STDDTN1WY1O9I', 35, '20250909001', 500.00, 0.00, 500.00, 1, '2025-09-09 07:50:22', NULL),
                                                                                                                             (5, 'STDV4GZZB1XMM', 36, '20250917001', 500.00, 0.00, 500.00, 1, '2025-09-17 13:40:19', NULL),
                                                                                                                             (6, 'STDZMMBTBZMF4', 37, '20260117001', 450.00, 0.00, 450.00, 1, '2026-01-17 02:21:51', NULL),
                                                                                                                             (7, 'STDQE419M1NND', 38, '20260117001', 1275.00, 0.00, 1275.00, 1, '2026-01-17 02:51:39', NULL),
                                                                                                                             (8, 'STDOM7N0SWDDT', 39, '20260118001', 1350.00, 0.00, 1350.00, 1, '2026-01-18 01:44:47', NULL),
                                                                                                                             (9, 'STDIHQB8EIZ39', 40, '20260118001', 425.00, 0.00, 425.00, 1, '2026-01-18 01:47:47', NULL),
                                                                                                                             (10, 'STD6FHEROZGIT', 41, '20260118001', 1350.00, 0.00, 1350.00, 1, '2026-01-18 01:53:44', NULL),
                                                                                                                             (11, 'STDIUYLIU54KR', 42, '20260118001', 1350.00, 0.00, 1350.00, 1, '2026-01-18 02:25:12', NULL),
                                                                                                                             (12, 'STDDRM10GZDL6', 43, '20260118001', 250.00, 0.00, 250.00, 1, '2026-01-18 02:45:45', NULL),
                                                                                                                             (13, 'STDQ3EU8EZ8VI', 44, '20260118001', 200.00, 0.00, 200.00, 1, '2026-01-18 03:01:27', NULL),
                                                                                                                             (14, 'STDVL7IYFG11R', 45, '20260118001', 150.00, 0.00, 150.00, 1, '2026-01-18 03:05:06', NULL),
                                                                                                                             (15, 'STD36EPL96UF9', 46, '20260118001', 150.00, 0.00, 150.00, 1, '2026-01-18 03:12:35', NULL),
                                                                                                                             (16, 'STDX9CLA4I1BG', 47, '20260118001', 100.00, 0.00, 100.00, 1, '2026-01-18 03:17:44', NULL),
                                                                                                                             (17, 'STDDE4MEASF75', 48, '20260121001', 200.00, 0.00, 200.00, 1, '2026-01-21 02:57:38', NULL),
                                                                                                                             (18, 'STD0DMVKQM7QA', 49, '20260121001', 1200.00, 0.00, 1200.00, 1, '2026-01-21 03:04:19', NULL),
                                                                                                                             (19, 'STDC44LXXA3HJ', 50, '20260121001', 750.00, 0.00, 750.00, 1, '2026-01-21 03:55:27', NULL),
                                                                                                                             (20, 'STDICFTAZ3ZAM', 51, '20260121001', 500.00, 0.00, 500.00, 1, '2026-01-21 04:10:51', NULL),
                                                                                                                             (21, 'STDEKUVLZ8W86', 52, '20260121001', 750.00, 0.00, 750.00, 1, '2026-01-21 04:19:33', NULL),
                                                                                                                             (22, 'STD3BAMFK82WO', 53, '20260121001', 250.05, 0.00, 250.05, 1, '2026-01-21 04:28:13', NULL);

SELECT setval('transactions_id_seq', (SELECT MAX(id) FROM transactions));

-- ============================================================
-- Table: type_payments
-- ============================================================
CREATE TABLE type_payments (
                               id SERIAL PRIMARY KEY,
                               name VARCHAR(150),
                               created TIMESTAMP,
                               created_by INTEGER,
                               modified TIMESTAMP,
                               modified_by INTEGER,
                               is_active SMALLINT DEFAULT 1
);

INSERT INTO type_payments (id, name, created, created_by, modified, modified_by, is_active) VALUES
                                                                                                (1, 'Cash', '2016-09-02 16:19:26', 1, NULL, NULL, 1),
                                                                                                (2, 'Bank', '2016-09-02 16:19:49', 1, NULL, NULL, 1),
                                                                                                (3, 'Cheque', '2016-09-02 16:19:49', 1, NULL, NULL, 1),
                                                                                                (4, 'AC', '2025-03-14 15:05:06', 1, NULL, NULL, 1);

SELECT setval('type_payments_id_seq', (SELECT MAX(id) FROM type_payments));

-- ============================================================
-- Table: users
-- ============================================================
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       sys_code VARCHAR(100),
                       pin VARCHAR(1000),
                       main_project_id BIGINT,
                       project_id BIGINT,
                       user_code VARCHAR(50),
                       session_id VARCHAR(255),
                       session_start TIMESTAMP,
                       session_active TIMESTAMP,
                       session_lat VARCHAR(50),
                       session_long VARCHAR(50),
                       session_accuracy VARCHAR(50),
                       login_attempt TIMESTAMP,
                       login_attempt_remote_ip VARCHAR(50),
                       login_attempt_http_user_agent VARCHAR(255),
                       login_lat VARCHAR(50),
                       login_long VARCHAR(50),
                       login_accuracy VARCHAR(50),
                       expired DATE,
                       duration BIGINT DEFAULT 0,
                       username VARCHAR(255),
                       password VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                       sex VARCHAR(7),
                       dob DATE,
                       address TEXT,
                       telephone VARCHAR(50),
                       email VARCHAR(255),
                       nationality INTEGER,
                       signature_photo VARCHAR(255),
                       created TIMESTAMP,
                       created_by BIGINT,
                       modified TIMESTAMP,
                       modified_by BIGINT,
                       is_hash SMALLINT DEFAULT 0,
                       is_sync SMALLINT DEFAULT 0,
                       is_active SMALLINT DEFAULT 1,
                       room_id INTEGER,
                       google_auth TEXT,
                       is_verify_auth SMALLINT DEFAULT 0,
                       tele_user_id VARCHAR(255),
                       verification_code VARCHAR(6),
                       verification_code_expiry TIMESTAMP,
                       type SMALLINT NOT NULL DEFAULT 1
);

CREATE INDEX idx_users_first_name ON users(first_name);
CREATE INDEX idx_users_last_name ON users(last_name);

INSERT INTO users (id, sys_code, pin, main_project_id, project_id, user_code, session_id, session_start, session_active, session_lat, session_long, session_accuracy, login_attempt, login_attempt_remote_ip, login_attempt_http_user_agent, login_lat, login_long, login_accuracy, expired, duration, username, password, first_name, last_name, sex, dob, address, telephone, email, nationality, signature_photo, created, created_by, modified, modified_by, is_hash, is_sync, is_active, room_id, google_auth, is_verify_auth, tele_user_id, verification_code, verification_code_expiry, type) VALUES
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (1, NULL, NULL, NULL, NULL, NULL, 'tim9jallsh0u69ukmjaq43s2a4', '2026-06-24 21:18:59', '2026-06-24 21:18:59', '', '', '', NULL, '203.189.156.156', 'OS: Windows 10 Browser: Gecko based', NULL, NULL, NULL, '2043-12-29', 0, 'super_admin', '$2a$10$e7P6W4j93OjKM8fO/.7KE.5tixsTePEIOw.66y5Wn8PGvppq1aw8u', 'super_admin', 'System', 'Male', '1985-02-02', 'PP', '011885858', 'admin@gmail.com', 36, '', '2017-02-17 09:33:11', 1, '2024-12-27 19:17:20', 1, 0, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 2),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (30, NULL, NULL, NULL, NULL, 'UME001', NULL, '2026-06-24 21:11:24', '2026-06-24 21:11:24', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'dalet', '$2a$10$lSiaVL3VpL.IfGrh8e8SVuoMMY6T4s9hc/iRlyIDDRI8RwYWk/kJG', 'Nang', 'Dalet', NULL, NULL, NULL, '070726363', 'daletnang001@gmail.com', NULL, NULL, '2025-07-22 11:40:58', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (31, NULL, NULL, NULL, NULL, 'UME002', NULL, '2026-06-24 21:11:40', '2026-06-24 21:11:40', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sarinlyza', '$2a$10$FEbd8rnPhAXkyI9LRwy5suxpzFL4BZomM4l8Se8KqfqHEKvXcFw1G', 'Sarin', 'Lyza', NULL, NULL, NULL, '098302338', 'sarinlyza@gmail.com', NULL, NULL, '2025-07-22 11:41:46', 30, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (32, NULL, NULL, NULL, NULL, 'UME003', NULL, '2026-01-11 15:47:10', '2026-01-11 15:47:10', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'da', '$2a$10$OszVWVd/xDtBNe6h2gkTPu4w4seN2tBFXtny3TXo5uDJtoGJ.E2ee', 'Chhan', 'Chhorda', NULL, NULL, NULL, NULL, 'da@gmail.com', NULL, NULL, '2025-08-27 15:56:57', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (33, NULL, NULL, NULL, NULL, 'UME004', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'vila', '$2a$10$vUmHCYRvlIdhFOTV2hPV0u2tO6.1mRaGdxVkmwXBlUFMqC.rPuh4.', 'Koun', 'Sovila', NULL, NULL, NULL, NULL, 'kounsovila@gmail.com', NULL, NULL, '2025-08-28 14:28:12', 1, '2026-01-18 10:35:26', 1, 0, 0, 2, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (34, NULL, NULL, NULL, NULL, 'UME005', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'lyka', '$2a$10$qJBpC/LIMPC.NX.VvqePgOv26H/bYFalGQTQYnaUQk/kwQwUL7iCK', 'Khoeun', 'Molyka', NULL, NULL, NULL, NULL, 'lyka@gmail.com', NULL, NULL, '2025-09-05 20:34:35', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (35, NULL, NULL, NULL, NULL, 'UME006', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'lida', '$2a$10$lYfEDitAySArFMkmCbSs8e5bqvqXPA3u.ScJWNL/VC29CdflrpU/.', 'Kim', 'Lida', NULL, NULL, NULL, NULL, 'lina@gmail.com', NULL, NULL, '2025-09-05 20:56:15', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (36, NULL, NULL, NULL, NULL, 'UME007', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'lina', '$2a$10$m9S7.9uPGZ/0y2o5Ih/V/.2ccJ5EZL38VgmWtrNvBJmEr0Wty6wRq', 'Kim', 'Lida', NULL, NULL, NULL, NULL, 'lida@gmail.com', NULL, NULL, '2025-09-09 13:42:42', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (37, NULL, NULL, NULL, NULL, 'UME008', NULL, '2025-09-29 14:46:53', '2025-09-29 14:46:53', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'neath', '$2a$10$ZtE0ap6WhgPXuCF1DoiUFeA7xwVUc3wyotphan1./D7mxeq2mvgyy', 'Neang', 'Sreyneath', NULL, NULL, NULL, NULL, 'neath@gmail.com', NULL, NULL, '2025-09-29 14:43:34', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (38, NULL, NULL, NULL, NULL, 'UME009', NULL, '2025-12-31 07:43:21', '2025-12-31 07:43:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'meth', '$2a$10$UEjjy0r2cqazOIi.DVB9ke52o6FR11Ka8tZl4ASPnEX8l8nxdo5a2', 'Khat', 'Someth', NULL, NULL, NULL, '081287798', 'meth@gmail.com', NULL, NULL, '2025-09-29 14:48:30', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (39, NULL, NULL, NULL, NULL, 'UME010', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sor', '$2a$10$jZ3MZpcHWD.jPv/NRLsfUOYyHgXG1v9MHgwYipTQu0dwf4fS4NOb6', 'Sem', 'Kesor', NULL, NULL, NULL, NULL, 'sor@gmail.com', NULL, NULL, '2025-09-29 15:01:42', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (40, NULL, NULL, NULL, NULL, 'UME011', NULL, '2025-12-31 10:24:06', '2025-12-31 10:24:06', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'neang', '$2a$10$rvheeA4E3ZWGO15qmbL6YuGY/Dp3pdh9LtstMn1U0zgrqZR.hbvBO', 'Chhim', 'Sreyneang', NULL, NULL, NULL, NULL, 'neang@gmail.com', NULL, NULL, '2025-12-31 10:23:50', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (41, NULL, NULL, NULL, NULL, 'UME012', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'Dav', '$2a$10$9TESMMW8mvXVYQh54jhX7u7RmYaiRj9xupvfj9h7mnmhSVGz.utRq', 'Kim', 'Dav', NULL, NULL, NULL, NULL, 'dav@gmail.com', NULL, NULL, '2026-01-15 16:16:41', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (42, NULL, NULL, NULL, NULL, 'UME013', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'saq', '$2a$10$K5ivkQLUMf6XYGsvFLuVMOkDXjh4ULheJPgcAUMvpD2jBeE1/.PYq', 'daq', 'dq', NULL, NULL, NULL, NULL, 'saq@gmail.com', NULL, NULL, '2026-01-15 16:19:38', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (43, NULL, NULL, NULL, NULL, 'UME014', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'xa', '$2a$10$PGOVzk.GRpwtyVH4S0nOYu/hltzTSgCj0YkNZCBhwoWB2NQo3ViNO', 'xa', 'xa', NULL, NULL, NULL, NULL, 'xa@gmail.com', NULL, NULL, '2026-01-15 16:21:06', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (44, NULL, NULL, NULL, NULL, 'UME015', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'safa', '$2a$10$eg1aEPM5InSArZEB.jORx.U4d17bdadcaNhduSKSqxy.8PyWCPH12', 'zad', 'xa', NULL, NULL, NULL, NULL, 'cdc@gmail.com', NULL, NULL, '2026-01-15 16:21:43', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (45, NULL, NULL, NULL, NULL, 'UME016', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'err', '$2a$10$ubhX8GVcbvz4KmsiZ0Jae.tPZXW29dCjIxzvDVH7Ux2yyNTn0qu06', 'err', 'err', NULL, NULL, NULL, NULL, 'err@gmail.com', NULL, NULL, '2026-01-15 16:25:49', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (46, NULL, NULL, NULL, NULL, 'UME017', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'vv', '$2a$10$EF1LQAFnsE6kgt6Q99xF1.VOW4UIYvoWFx9V3svnEdOV523xnJk96', 'vv', 'vv', NULL, NULL, NULL, NULL, 'vv@gmail.com', NULL, NULL, '2026-01-15 16:26:31', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (47, NULL, NULL, NULL, NULL, 'UME018', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sssss', '$2a$10$PE.d3o72a8wS/tBQ9wXksO0ovqcd1RomKGcbhR6JWVZzfDYO3DS2S', 'ssss', 'sssss', NULL, NULL, NULL, NULL, 'sssss@gmail.com', NULL, NULL, '2026-01-15 16:30:03', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (48, NULL, NULL, NULL, NULL, 'UME019', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sav', '$2a$10$VfjD7vpZMmbcBtctx2sNQeEuVjMSYOykVnNIWdPrFZ5V7yH2vWGiy', 'dav', 'dav', NULL, NULL, NULL, NULL, 'asa@gmail.com', NULL, NULL, '2026-01-15 16:31:13', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (49, NULL, NULL, NULL, NULL, 'UME020', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'ty', '$2a$10$1aG4aEqfBqrTDcZrBW91NeW4SEbvD/4.JcuAHdTmVaAPihO.dEP96', 'ty', 'yt', NULL, NULL, NULL, NULL, 'ty@gmail.com', NULL, NULL, '2026-01-15 16:42:36', 1, '2026-01-16 11:35:09', 1, 0, 0, 2, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (50, NULL, NULL, NULL, NULL, 'UME021', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sweda', '$2a$10$0vVLKmTI9MuKAtrTq0O59OBrO63CX2ZOvgnadEdqJLqPCBFWn6Wea', 'sssok', 'sdfa', NULL, NULL, NULL, NULL, 'adad@gmail.com', NULL, NULL, '2026-01-15 16:45:15', 1, '2026-01-15 16:46:42', 1, 0, 0, 2, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (51, NULL, NULL, NULL, NULL, 'UME022', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'saz', '$2a$10$dgkaNFap3iM7EEBPdAakeOmZ/W7xJ98Z8lCo3h0tC6DdIUysIyVQq', 'sa', 'sa', NULL, NULL, NULL, NULL, 'sad@gmail.com', NULL, NULL, '2026-01-16 11:52:21', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1),
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         (52, NULL, NULL, NULL, NULL, 'UME023', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, 'sokha', '$2a$10$th92Sbj/dzQsGuVkcipCVeVqsmibbOIG1.SGSsrGfsyjv6Clc9lNm', 'Chhan', 'Sokha', NULL, NULL, NULL, NULL, 'sokha@gmail.com', NULL, NULL, '2026-01-16 11:56:22', 1, NULL, NULL, 0, 0, 1, NULL, NULL, 0, NULL, NULL, NULL, 1);

SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));

-- ============================================================
-- Table: user_branches
-- ============================================================
CREATE TABLE user_branches (
                               id SERIAL PRIMARY KEY,
                               user_id INTEGER,
                               branch_id INTEGER
);

CREATE INDEX idx_user_branches_user_branch ON user_branches(user_id, branch_id);

INSERT INTO user_branches (id, user_id, branch_id) VALUES
                                                       (207, 0, 6),
                                                       (234, 0, 7),
                                                       (231, 0, 8),
                                                       (222, 0, 9),
                                                       (223, 0, 10),
                                                       (224, 0, 11),
                                                       (225, 0, 12),
                                                       (226, 0, 13),
                                                       (300, 0, 14),
                                                       (236, 1, 1),
                                                       (202, 1, 4),
                                                       (229, 1, 5),
                                                       (237, 1, 20),
                                                       (208, 21, 1),
                                                       (238, 21, 4),
                                                       (242, 25, NULL),
                                                       (243, 26, NULL),
                                                       (244, 27, NULL),
                                                       (245, 27, NULL),
                                                       (246, 27, NULL),
                                                       (247, 27, NULL),
                                                       (248, 28, NULL),
                                                       (249, 28, NULL),
                                                       (250, 28, NULL),
                                                       (251, 28, NULL),
                                                       (252, 29, NULL),
                                                       (253, 29, NULL),
                                                       (254, 29, NULL),
                                                       (255, 29, NULL),
                                                       (256, 30, NULL),
                                                       (257, 30, NULL),
                                                       (258, 30, NULL),
                                                       (259, 30, NULL),
                                                       (260, 31, NULL),
                                                       (261, 31, NULL),
                                                       (262, 31, NULL),
                                                       (263, 31, NULL),
                                                       (264, 32, NULL),
                                                       (265, 32, NULL),
                                                       (266, 32, NULL),
                                                       (267, 32, NULL),
                                                       (268, 33, NULL),
                                                       (269, 33, NULL),
                                                       (270, 33, NULL),
                                                       (271, 33, NULL),
                                                       (272, 34, NULL),
                                                       (273, 34, NULL),
                                                       (274, 34, NULL),
                                                       (275, 34, NULL),
                                                       (276, 35, NULL),
                                                       (277, 35, NULL),
                                                       (278, 35, NULL),
                                                       (279, 35, NULL),
                                                       (280, 36, NULL),
                                                       (281, 36, NULL),
                                                       (282, 36, NULL),
                                                       (283, 36, NULL),
                                                       (284, 37, NULL),
                                                       (285, 37, NULL),
                                                       (286, 37, NULL),
                                                       (287, 37, NULL),
                                                       (288, 38, NULL),
                                                       (289, 38, NULL),
                                                       (290, 38, NULL),
                                                       (291, 38, NULL),
                                                       (292, 39, NULL),
                                                       (293, 39, NULL),
                                                       (294, 39, NULL),
                                                       (295, 39, NULL),
                                                       (296, 40, NULL),
                                                       (297, 40, NULL),
                                                       (298, 40, NULL),
                                                       (299, 40, NULL),
                                                       (301, 41, NULL),
                                                       (302, 41, NULL),
                                                       (303, 41, NULL),
                                                       (304, 41, NULL),
                                                       (305, 42, NULL),
                                                       (306, 42, NULL),
                                                       (307, 42, NULL),
                                                       (308, 42, NULL),
                                                       (309, 43, NULL),
                                                       (310, 43, NULL),
                                                       (311, 43, NULL),
                                                       (312, 43, NULL),
                                                       (313, 44, NULL),
                                                       (314, 44, NULL),
                                                       (315, 44, NULL),
                                                       (316, 44, NULL),
                                                       (317, 45, NULL),
                                                       (318, 45, NULL),
                                                       (319, 45, NULL),
                                                       (320, 45, NULL),
                                                       (321, 46, NULL),
                                                       (322, 46, NULL),
                                                       (323, 46, NULL),
                                                       (324, 46, NULL),
                                                       (325, 47, NULL),
                                                       (326, 47, NULL),
                                                       (327, 47, NULL),
                                                       (328, 47, NULL),
                                                       (329, 48, NULL),
                                                       (330, 48, NULL),
                                                       (331, 48, NULL),
                                                       (332, 48, NULL),
                                                       (333, 49, NULL),
                                                       (334, 49, NULL),
                                                       (335, 49, NULL),
                                                       (336, 49, NULL),
                                                       (337, 50, NULL),
                                                       (338, 50, NULL),
                                                       (339, 50, NULL),
                                                       (340, 50, NULL),
                                                       (341, 51, NULL),
                                                       (342, 51, NULL),
                                                       (343, 51, NULL),
                                                       (344, 51, NULL),
                                                       (345, 52, NULL),
                                                       (346, 52, NULL),
                                                       (347, 52, NULL),
                                                       (348, 52, NULL);

SELECT setval('user_branches_id_seq', (SELECT MAX(id) FROM user_branches));

-- ============================================================
-- Table: user_groups
-- ============================================================
CREATE TABLE user_groups (
                             id SERIAL PRIMARY KEY,
                             user_id INTEGER,
                             group_id INTEGER
);

CREATE INDEX idx_user_groups_user_group ON user_groups(user_id, group_id);

INSERT INTO user_groups (id, user_id, group_id) VALUES
                                                    (432, 0, 2),
                                                    (481, 0, 14),
                                                    (520, 1, 1),
                                                    (475, 1, 3),
                                                    (429, 1, 5),
                                                    (338, 2, 2),
                                                    (438, 22, 2),
                                                    (442, 24, 5),
                                                    (446, 27, 2),
                                                    (449, 29, 2),
                                                    (521, 30, 1),
                                                    (451, 30, 2),
                                                    (483, 30, 16),
                                                    (497, 30, 21),
                                                    (516, 30, 23),
                                                    (484, 31, 4),
                                                    (495, 31, 19),
                                                    (476, 32, 3),
                                                    (462, 32, 6),
                                                    (482, 32, 15),
                                                    (498, 32, 21),
                                                    (519, 32, 24),
                                                    (467, 33, 12),
                                                    (499, 33, 21),
                                                    (477, 35, 3),
                                                    (478, 36, 3),
                                                    (474, 37, 13),
                                                    (518, 39, 24),
                                                    (522, 40, 1),
                                                    (480, 40, 12),
                                                    (501, 42, 3),
                                                    (502, 43, 3),
                                                    (503, 44, 5),
                                                    (504, 45, 3),
                                                    (505, 46, 3),
                                                    (506, 47, 3),
                                                    (507, 48, 3),
                                                    (508, 49, 3),
                                                    (509, 50, 3),
                                                    (523, 51, 3),
                                                    (524, 52, 5);

SELECT setval('user_groups_id_seq', (SELECT MAX(id) FROM user_groups));

-- ============================================================
-- Verification
-- ============================================================
DO $$
DECLARE
table_count INTEGER;
BEGIN
    RAISE NOTICE 'Migration completed successfully!';

SELECT COUNT(*) INTO table_count FROM information_schema.tables
WHERE table_schema = 'public' AND table_name NOT LIKE 'pg_%';
RAISE NOTICE 'Total tables created: %', table_count;
END $$;