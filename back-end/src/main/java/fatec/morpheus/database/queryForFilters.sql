SELECT 
    n.new_content
FROM 
    news n 
LEFT JOIN 
    news_author a ON a.new_aut_id = n.new_aut_cod 
WHERE 
    new_aut_id IN (1)
LIMIT 3 OFFSET 0;

SELECT 
    n.new_content 
FROM 
    news n 
WHERE 
    n.new_src_cod IN (1)
LIMIT 3 OFFSET 0;

SELECT 
    new_content 
FROM 
    news 
WHERE 
    new_content LIKE '%palavra%'
LIMIT 3 OFFSET 0;

SELECT 
    n.new_content 
FROM 
    news n 
WHERE 
    new_title IN ("", "")
LIMIT 1 OFFSET 0;

SELECT 
    new_content 
FROM 
    news 
WHERE 
    new_registry_date BETWEEN '2024-01-10' AND '2024-12-31'
LIMIT 5 OFFSET 0;