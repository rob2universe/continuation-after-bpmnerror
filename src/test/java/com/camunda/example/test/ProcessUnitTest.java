package com.camunda.example.test;

import com.camunda.example.service.LoggerDelegate;
import com.camunda.example.service.ProcessModifierDelegate;
import com.camunda.example.service.ProcessStarterDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.spin.SpringBootSpinProcessEnginePlugin;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;


@Deployment(resources = "process.bpmn")
public class ProcessUnitTest {

    @Rule
    public ProcessEngineRule engine = new ProcessEngineRule();

    @Before
    public void setUp() {

        Mocks.register("logger", new LoggerDelegate());
        Mocks.register("starter", new ProcessStarterDelegate());
        Mocks.register("modifier", new ProcessModifierDelegate());
    }

    public void testBPMNErrorAndContinuation(String taskID) {

        ProcessInstance pi = runtimeService().startProcessInstanceByKey("Process_RequestProcessing", withVariables("failAt", taskID));
        assertThat(pi).hasPassed("StartErrorHandlingEvent")
            .isWaitingAt("ErrorHandlingCompletedEvent");

        ProcessInstance errorHandlingPI = processInstanceQuery().processDefinitionKey("Process_ManualErrorHandling").singleResult();
        assertThat(errorHandlingPI).isWaitingAt("ResolveErrorTask");
        complete(task(), withVariables("continuation", true));
        assertThat(errorHandlingPI).hasPassed("OriginalProcessContinuedEndEvent").isEnded();

        assertThat(pi).hasPassed("FirstServiceTask","SecondServiceTask","ThirdServiceTask").isEnded();
    }

    @Test
    public void testBPMNErrorAndContinuationAtFirstTask() {
        testBPMNErrorAndContinuation("FirstServiceTask");
    }

    @Test
    public void testBPMNErrorAndContinuationAtSecondTask() {
        testBPMNErrorAndContinuation("SecondServiceTask");
    }

    @Test
    public void testBPMNErrorAndContinuationAtThirdTask() {
        testBPMNErrorAndContinuation("ThirdServiceTask");
    }
}
