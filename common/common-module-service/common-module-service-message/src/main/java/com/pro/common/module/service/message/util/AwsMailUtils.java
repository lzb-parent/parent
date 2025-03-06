//package com.pro.common.module.service.message.util;
//
//import cn.hutool.json.JSONObject;
//import com.pro.common.modules.api.dependencies.exception.BusinessException;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.mail.MessagingException;
//import javax.print.attribute.standard.Destination;
//import javax.swing.plaf.synth.Region;
//import java.io.IOException;
//
///**
// * aws
// * https://docs.aws.amazon.com/zh_cn/ses/latest/dg/send-email-api.html
// * https://github.com/awsdocs/aws-doc-sdk-examples/blob/main/javav2/example_code/ses/src/main/java/com/example/ses/SendMessage.java
// */
//@Slf4j
//public class AwsMailUtils {
//
//    public static JSONObject sendEmail(
//            String appid,
//            String appkey,
//            String from,
//            String to,
//            String title,
//            String content) {
//        JSONObject jo = new JSONObject();
//        try {
//            Region region = Region.US_EAST_2;
//            System.setProperty("aws.accessKeyId", appid);
//            System.setProperty("aws.secretAccessKey", appkey);
//            SesClient client = SesClient.builder()
//                    .region(region)
//                    .build();
//            SendEmailResponse result = send(client, from, to, title, content);
//            client.close();
//            jo.put("messageId", result.messageId());
//        } catch (IOException | MessagingException e) {
//            e.getStackTrace();
//        }
//        return jo;
//    }
//
//    public static SendEmailResponse send(SesClient client,
//                                         String sender,
//                                         String recipient,
//                                         String subject,
//                                         String bodyHTML
//    ) throws MessagingException, IOException {
//
//        Destination destination = Destination.builder()
//                .toAddresses(recipient)
//                .build();
//
//        Content content = Content.builder()
//                .data(bodyHTML)
//                .build();
//
//        Content sub = Content.builder()
//                .data(subject)
//                .build();
//
//        Body body = Body.builder()
//                .html(content)
//                .build();
//
//        Message msg = Message.builder()
//                .subject(sub)
//                .body(body)
//                .build();
//
//        SendEmailRequest emailRequest = SendEmailRequest.builder()
//                .destination(destination)
//                .message(msg)
//                .source(sender)
//                .build();
//        try {
//            return client.sendEmail(emailRequest);
//        } catch (SesException e) {
//            System.err.println(e.awsErrorDetails().errorMessage());
//            throw new BusinessException(e.awsErrorDetails().errorMessage());
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//    }
//
//
//}
