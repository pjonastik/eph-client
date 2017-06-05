package sk.fri.uniza.utils;

import org.junit.Test;
import sk.fri.uniza.utils.DateUtil;

import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DateUtilTest {

    @Test
    public void shouldParseTimestamp() throws Exception {
        Date d = DateUtil.parseTimeStamp("2017-03-29 13:39:29");
        assertThat(d.toString(), is("Wed Mar 29 13:39:29 CEST 2017"));
    }

    @Test(expected = DateUtil.WrongTimpeStampExcpetion.class)
    public void parseDate_ShouldFail() throws Exception {
        DateUtil.parseTimeStamp("2017-03-29");
    }

    @Test
    public void shouldParseDate() throws Exception {
        Date d = DateUtil.parseDate("2017-03-29");
        assertThat(d.toString(), is("Wed Mar 29 00:00:00 CEST 2017"));
    }

    @Test(expected = DateUtil.WrongTimpeStampExcpetion.class)
    public void parseDateShouldFail() throws Exception {
        DateUtil.parseDate("2017-03-");
    }
}
