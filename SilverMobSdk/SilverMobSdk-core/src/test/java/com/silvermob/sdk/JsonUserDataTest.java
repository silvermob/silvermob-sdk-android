package com.silvermob.sdk;

import com.silvermob.sdk.api.data.AdFormat;
import com.silvermob.sdk.configuration.AdUnitConfiguration;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class JsonUserDataTest {

    @Test
    public void putUsersDataListToRequestParams_addElementsToOutsideList_elementsAddedToRequestParams() {
        ArrayList<DataObject> userDataObjects = new ArrayList<>();
        DataObject.SegmentObject segment = new DataObject.SegmentObject();
        segment.setId("segmentId1");
        segment.setName("segmentName1");
        segment.setValue("segmentValue1");
        DataObject dataObject = new DataObject();
        dataObject.setId("testId1");
        dataObject.setName("testName1");
        dataObject.addSegment(segment);
        userDataObjects.add(dataObject);

        AdUnitConfiguration configuration = new AdUnitConfiguration();
        configuration.setConfigId("configId");
        configuration.setAdFormat(AdFormat.BANNER);
        for (DataObject data : userDataObjects) {
            configuration.addUserData(data);
        }

        assertEquals(1, configuration.getUserData().size());
        assertThat(configuration.getUserData(), Matchers.contains(dataObject));

        DataObject secondObject = new DataObject();
        secondObject.setId("secondId");
        configuration.addUserData(secondObject);

        assertEquals(2, configuration.getUserData().size());
        assertThat(configuration.getUserData(), CoreMatchers.hasItems(
                dataObject,
                secondObject
        ));
    }

}
