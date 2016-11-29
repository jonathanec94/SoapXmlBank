/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Jonathan
 */
@WebService(serviceName = "LoanResponseService")
public class LoanResponseService {

    

    /**
     * Web service operation
     *
     * @param i
     * @param j
     * @param p
     * @param l
     * @return
     */
    @WebMethod(operationName = "loanResponse")
    public boolean loanResponse(@WebParam(name = "ssn") int i, @WebParam(name = "creditScore") int j, @WebParam(name = "loanAmount") double p, @WebParam(name = "loanDuration") Date l,@WebParam(name = "responseQueue") String responseQueue) {
        //TODO write your implementation code here:
        double rangeMin = 1.5;
        double rangeMax = 7.3;
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(responseQueue, "direct");

            String routingKey = "info";
            String message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><LoanResponse>    <interestRate>"+randomValue+"</interestRate>    <ssn>"+j+"</ssn> </LoanResponse> ";

            channel.basicPublish(responseQueue, routingKey, null, message.getBytes());
           // System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

            channel.close();
            connection.close();
            return true;
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(LoanResponseService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }
}
