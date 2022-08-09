package com.shop.user;

import com.shop.entity.User;
import org.springframework.format.datetime.DateFormatter;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter {
    public void export(List<User> listUser, HttpServletResponse response) throws IOException {

        DateFormat dateFormatter = new SimpleDateFormat("YYYY-MM-yy_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "users_" + timestamp + ".csv";
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + fileName;
        response.setHeader(headerKey,headerValue);

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        String [] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String [] fieldMapper = {"id","email","firstName","lastName","Roles","Enabled"};

        csvBeanWriter.writeHeader(csvHeader);
        for (User user : listUser){
            csvBeanWriter.write(user,fieldMapper);
        }
        csvBeanWriter.close();
    }
}
