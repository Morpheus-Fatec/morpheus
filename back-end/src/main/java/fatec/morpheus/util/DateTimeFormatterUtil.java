package fatec.morpheus.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.ZoneId;
import java.sql.Timestamp;

public class DateTimeFormatterUtil {

    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    public Date getDate(String dateTimeString) {
        // Cria um DateTimeFormatter com o formato especificado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        
        // Converte a string para LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        
        // Converte LocalDateTime para java.util.Date
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getDateString(Date date) {
        // Converte java.util.Date para LocalDateTime
        LocalDateTime localDateTime = new Timestamp(date.getTime()).toLocalDateTime();
        
        // Cria um DateTimeFormatter para formatar de volta para string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        
        return localDateTime.format(formatter);
    }
}
