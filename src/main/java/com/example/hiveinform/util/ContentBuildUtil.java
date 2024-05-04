package com.example.hiveinform.util;

import com.example.hiveinform.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentBuildUtil {

    @Autowired
    private RedisService redisService ;

    public String buildContent(String to,String control)
    {
        String code = getRandomCode();
        redisService.save(code.toLowerCase() , to , 180);    // redis 存储 code ： Email
        String contents = " <html>" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "</head> <body> <div style=\"position: absolute; width: 850px; height: 1200px; left: 10%; background-color: rgba(12, 12, 12, 0.778); padding-top: 5%; padding-left: 150px;\">\n" +
                "        <h1 style=\"color: cornflowerblue; position: relative; font-size: 65px; \"> Hive Inform :</h1>\n" +
                "        <div style=\"font-size: 24px; color: azure;\">\n" +
                "        Dear user , you are "+control+" <a href=\"#\">HiveInform</a>.\n" +
                "        <br>\n" +
                "        <br>\n" +
                "        <br>\n" +
                "        The Confirm Code:\n" +
                "        <br>\n" +
                "        <div style=\"background-color: rgba(57, 160, 219, 0.795); width: 500px; height: 200px; position: relative; left: 5%; margin-top: 5%;\">\n" +
                "                    <p style=\"font-size: 100px; color: aliceblue; position: relative; top: 15px; left: 45px;\">"+code.toUpperCase()+"</p>\n" +
                "        </div>\n" +
                "        <div style=\"font-size: 24px; color: azure;position: relative; left: 5%;\"> \n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            expiration date: 3 min\n" +
                "            <br><br><br><br><br><br>\n" +
                "            <br>\n" +
                "            <br>\n" +
                "            <br>\n" +
                "        </div>\n" +
                "        </div>\n" +
                "    </div> </body>  </html>" ;
        return contents ;
    }

    public String getRandomCode()
    {
        UuidUtil uuidUtil = new UuidUtil();
        String s = null;

        do{
            s = uuidUtil.get().substring(2,8);
        }while (redisService.get(s) == null) ;

        return s ;
    }
}
