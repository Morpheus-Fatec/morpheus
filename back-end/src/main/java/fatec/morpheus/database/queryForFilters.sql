SELECT 
*
FROM news n
LEFT JOIN news_author a
ON n.new_aut_cod = a.new_aut_id
LEFT JOIN source s
ON n.new_src_cod = s.src_cod 
WHERE a.new_aut_id IN (1, 2, 5) AND
s.src_cod IN (1) AND
n.new_content REGEXP "por" AND
n.new_title REGEXP "Veja imagens de confronto |Varela" AND
n.new_registry_date BETWEEN '2024-01-10' AND '2024-12-31'
LIMIT 2 OFFSET 0;