package cn.tripod.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import cn.itcast.bos.utils.SendMsgUtils;

@Service("smsConsumer")
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		//接收MQ中的消息  并将message强转为MapMessage类型
		MapMessage mapMessage = (MapMessage)message;
		try {
			SendMsgUtils.sendMsg(mapMessage.getString("telephone"), mapMessage.getString("telcode"));
			System.out.println("短信码发出...");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

}
