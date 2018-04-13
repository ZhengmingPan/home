package com.home.core.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.activemq.ScheduledMessage;

/**
 * 消息延时发送参数
 * 
 * @author Administrator
 *
 */
public class AmqScheduled {
 
	private static final String PATTERN_CRON = "(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7])|([1-7])|(\\?)|(\\*)|(([1-7]L)|([1-7]\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7] )|([1-7] )|(\\? )|(\\* )|(([1-7]L )|([1-7]\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\-(19[789][0-9]|20[0-9][0-9])))";

	private Map<String, Object> propertiesMap;

	/**
	 * Jms定时发送构造函数
	 * 
	 * @param delay
	 *            延迟投递的时间
	 * @param period
	 *            重复投递的时间间隔
	 * @param repeat
	 *            重复投递次数
	 * @param cron
	 *            Cron表达式
	 */
	private AmqScheduled(Long delay, Long period, Integer repeat, String cron) {
		propertiesMap = new HashMap<String, Object>();
		if (validNumber(delay)) {
			propertiesMap.put(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
		}
		if (validNumber(period)) {
			propertiesMap.put(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
		}
		if (validNumber(repeat)) {
			propertiesMap.put(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
		}
		if (validCronExpression(cron)) {
			propertiesMap.put(ScheduledMessage.AMQ_SCHEDULED_CRON, cron);
		}
	}

	/**
	 * 得到关于消息的所有配置项
	 * 
	 * @return
	 */
	public Map<String, Object> getPropertiesMap() {
		return propertiesMap;
	}

	/**
	 * 验证Number是否合法
	 * 
	 * @param item
	 * @return
	 */
	private static boolean validNumber(Number item) {
		return item != null && item.longValue() > 0;
	}

	/**
	 * 验证Cron表达式是否合法
	 * 
	 * @param item
	 * @return
	 */
	private static boolean validCronExpression(String cron) {
		return cron != null && !cron.isEmpty() && Pattern.compile(PATTERN_CRON).matcher(cron).find();
	}

}
