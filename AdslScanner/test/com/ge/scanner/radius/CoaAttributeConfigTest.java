package com.ge.scanner.radius;

import org.junit.Test;

import java.util.List;

/**
 * Created by falcon on 17-1-5.
 *
 */
public class CoaAttributeConfigTest {
    @Test
    public void getAttributes() throws Exception {
        List<Attribute> attributes = CoaAttributeConfig.getInstance().getAttributes(2011, "lock");
        attributes.forEach(System.out::println);
    }

}