import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRule;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.scenario.ProcessScenario;
import org.camunda.bpm.scenario.Scenario;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;
import static org.mockito.Mockito.when;

@Deployment(resources = {"process.bpmn"})
public class CalculadoraTest {

    @Rule
    @ClassRule
    public static TestCoverageProcessEngineRule rule = TestCoverageProcessEngineRuleBuilder.create()
            .assertClassCoverageAtLeast(0.5)
            .build();

    @Mock
    private ProcessScenario calculadora;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSomarComSucesso(){

        when(calculadora.waitsAtUserTask("Activity_FormularioCalculadora"))
                .thenReturn(taskDelegate -> {
                    taskDelegate.complete(withVariables("operacao", "soma", "a", 20, "b", 22));
                });

        when(calculadora.waitsAtUserTask("Task_Resultado")).thenReturn(taskDelegate -> taskDelegate.complete());

        ProcessInstance instance = Scenario.run(calculadora)
                .startByKey("calculadora")
                .execute()
                .instance(calculadora);

        Object resultado = historyService().createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getId())
                .variableName("resultado")
                .singleResult()
                .getValue();

        long valor = (long) Double.parseDouble(resultado.toString());

        Assertions.assertThat(valor).isEqualTo(42);

    }

    @Test
    public void deveSubtrairComSucesso(){

        when(calculadora.waitsAtUserTask("Activity_FormularioCalculadora"))
                .thenReturn(taskDelegate -> {
                    taskDelegate.complete(withVariables("operacao", "subtracao", "a", 20, "b", 22));
                });

        when(calculadora.waitsAtUserTask("Task_Resultado")).thenReturn(taskDelegate -> taskDelegate.complete());

        ProcessInstance instance = Scenario.run(calculadora)
                .startByKey("calculadora")
                .execute()
                .instance(calculadora);

        Object resultado = historyService().createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getId())
                .variableName("resultado")
                .singleResult()
                .getValue();

        long valor = (long) Double.parseDouble(resultado.toString());

        Assertions.assertThat(valor).isEqualTo(-2);

    }

    @Test
    public void deveMultiplicarComSucesso(){

        when(calculadora.waitsAtUserTask("Activity_FormularioCalculadora"))
                .thenReturn(taskDelegate -> {
                    taskDelegate.complete(withVariables("operacao", "multiplicacao", "a", 20, "b", 22));
                });

        when(calculadora.waitsAtUserTask("Task_Resultado")).thenReturn(taskDelegate -> taskDelegate.complete());

        ProcessInstance instance = Scenario.run(calculadora)
                .startByKey("calculadora")
                .execute()
                .instance(calculadora);

        Object resultado = historyService().createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getId())
                .variableName("resultado")
                .singleResult()
                .getValue();

        long valor = (long) Double.parseDouble(resultado.toString());

        Assertions.assertThat(valor).isEqualTo(440);

    }

    @Test
    public void deveDividirComSucesso(){

        when(calculadora.waitsAtUserTask("Activity_FormularioCalculadora"))
                .thenReturn(taskDelegate -> {
                    taskDelegate.complete(withVariables("operacao", "divisao", "a", 20, "b", 2));
                });

        when(calculadora.waitsAtUserTask("Task_Resultado")).thenReturn(taskDelegate -> taskDelegate.complete());

        ProcessInstance instance = Scenario.run(calculadora)
                .startByKey("calculadora")
                .execute()
                .instance(calculadora);

        Object resultado = historyService().createHistoricVariableInstanceQuery()
                .processInstanceId(instance.getId())
                .variableName("resultado")
                .singleResult()
                .getValue();

        long valor = (long) Double.parseDouble(resultado.toString());

        Assertions.assertThat(valor).isEqualTo(10);

    }
}
