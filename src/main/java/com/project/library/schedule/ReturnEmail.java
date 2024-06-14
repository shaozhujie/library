package com.project.library.schedule;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.library.domain.LibraryBorrow;
import com.project.library.domain.User;
import com.project.library.service.LibraryBorrowService;
import com.project.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author AA
 * @version 1.0
 * @description: TODO
 * @date 2024/5/29 16:33
 */
@Component
public class ReturnEmail {

    @Autowired
    private LibraryBorrowService libraryBorrowService;
    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void returnEmail() throws ParseException, MessagingException {
        //到期时间减去1天，如果当前日期大于到期时间，则发送邮件通知
        QueryWrapper<LibraryBorrow> query = new QueryWrapper<>();
        query.lambda().eq(LibraryBorrow::getState,2);
        List<LibraryBorrow> borrowList = libraryBorrowService.list(query);
        QueryWrapper<LibraryBorrow> query1 = new QueryWrapper<>();
        query1.lambda().eq(LibraryBorrow::getState,4);
        List<LibraryBorrow> borrowList1 = libraryBorrowService.list(query1);
        borrowList.addAll(borrowList1);
        for (LibraryBorrow borrow : borrowList) {
            Date date = borrow.getReturnTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String format = sdf.format(date) + " 23:59:59";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(format);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.DAY_OF_YEAR,-1);
            Date calendarTime = calendar.getTime();
            //获取当前时间
            Date today = new Date();
            User user = userService.getById(borrow.getUserId());
            if (today.after(calendarTime)) {
                JavaMailSenderImpl sender = new JavaMailSenderImpl();
                sender.setPort(25);
                sender.setHost("smtp.qq.com");
                sender.setUsername("1760272627@qq.com");
                sender.setPassword("nwavnzopbtpibchc");
                sender.setDefaultEncoding("utf-8");
                MimeMessage msg = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(msg, true);
                helper.setFrom(sender.getUsername());
                helper.setTo(user.getEmail());
                helper.setSubject("图书归还提醒");
                if (borrow.getState() == 2) {
                    helper.setText("您借阅的图书："+ borrow.getName() + "即将到期，请及时归还",true);
                } else {
                    helper.setText("您借阅的图书："+ borrow.getName() + "逾期尚未归还，请及时归还，若因图书丢失等原因导致无法归还，需赔偿与图书售价相同的金额",true);
                }
                sender.send(msg);
            }
        }
    }

}
