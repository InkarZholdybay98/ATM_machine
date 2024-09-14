UPDATE bankClients SET history_of_operations = 
CONCAT( COALESCE(history_of_operations , " ") , '\n' , ?)
WHERE atm_number = ?;
