--TODO 1
--Which city has the most invoices?
--Order by invoice count
SELECT BillingCity, COUNT(InvoiceId) InvoiceCount
FROM invoices i 
GROUP BY BillingCity 
ORDER BY InvoiceCount DESC;

--TODO 2
--Which cities has the best customers
--This means we want to have an ordered list
--5 best cities with highest sum of totals
SELECT BillingCity, SUM(total) TotalSum 
FROM invoices i 
GROUP BY BillingCity 
ORDER BY TotalSum DESC
LIMIT 5;

--TODO 3 Find the biggest 3 spenders
--this might involve joining customers and invoices and invoice items
--then using GROUP BY and then SUM on grouped TOTAL

SELECT c.CustomerId, 
c.FirstName, 
c.LastName, 
SUM(Total) TotalSum, 
COUNT(InvoiceId) InvoiceCount, 
GROUP_CONCAT(InvoiceId, ',') Invoices
FROM invoices i 
JOIN customers c
ON i.CustomerId = c.CustomerId 
GROUP BY c.CustomerId 
ORDER BY TotalSum DESC 
LIMIT 3;

--TODO 4 find ALL listeners to classical music
-- include their names and emails and phone numbers
--this might not need aggregation

SELECT DISTINCT FirstName, LastName, Email, Phone, g.Name Genre
FROM customers c 
JOIN invoices i ON c.CustomerId = i.CustomerId 
JOIN invoice_items ii ON i.InvoiceId = ii.InvoiceId 
JOIN tracks t ON ii.TrackId = t.TrackId 
JOIN genres g ON t.GenreId = g.GenreId 
WHERE g.Name LIKE 'classic%';
