package com.camunda.example.test;

import com.camunda.example.service.LoggerDelegate;
import com.camunda.example.service.ProcessStarterDelegate;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Before;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

@Deployment(resources = "process.bpmn")
public class ProcessUnitTest extends AbstractProcessEngineRuleTest {

    @Before
    public void setUp() throws Exception {
        processEngine.getProcessEngineConfiguration().setProcessEnginePlugins(....);

        Mocks.register("logger", new LoggerDelegate());
        Mocks.register("starter", new ProcessStarterDelegate());
    }

    @Test
    public void testBPMNError() {

        runtimeService().startProcessInstanceByKey("Process_RequestProcessing", withVariables("failAt", "SecondServiceTask" ));


    }
}
