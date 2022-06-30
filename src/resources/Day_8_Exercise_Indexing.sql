--TODO 1
-- create index for milliseconds on tracks table
-- can it use unique constraint?
-- query all tracks over 5 min length
-- check if the query uses index

SELECT * FROM tracks t ;

SELECT COUNT(Name) Number_of_tracks, Milliseconds 
FROM tracks t 
GROUP BY Milliseconds
ORDER BY Number_of_tracks DESC;

-- so we can't use unique constraint because of repeating values

CREATE INDEX idx_milliseconds
ON tracks(Milliseconds);

SELECT * FROM tracks t 
WHERE Milliseconds / 1000 / 60 > 5;

EXPLAIN QUERY PLAN
SELECT * FROM tracks t 
WHERE Milliseconds / 1000 / 60 > 5;
-- query doesn't use index 

--TODO 2
-- create index on combined LENGTH of customers first_name and last_name
-- two possible approaches one with concat one without
-- find all customers with combined length over 20 symbols
-- check if the query uses index

SELECT * FROM customers;

CREATE INDEX idx_length_of_names
ON customers(LENGTH (FirstName || LastName));

SELECT FirstName, LastName FROM customers c
WHERE LENGTH (FirstName || LastName) > 20;
-- there are no such customers with long names

EXPLAIN QUERY PLAN
SELECT FirstName, LastName FROM customers c
WHERE LENGTH (FirstName || LastName) > 20;
--query uses index