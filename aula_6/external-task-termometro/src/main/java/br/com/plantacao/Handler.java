package br.com.plantacao;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Component
@ExternalTaskSubscription("termometro")
public class Handler implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Integer temperatura = null;
        try {
            temperatura = restTemplate.getForObject("http://wiremock:8080/temperatura", Integer.class);

            processarTemperatura(externalTask, externalTaskService, temperatura);

        } catch (Exception e) {
            externalTaskService.handleFailure(externalTask, "Erro ao consultar temperatura", e.getMessage(), 0, 0L);
        }

    }

    private void processarTemperatura(ExternalTask externalTask, ExternalTaskService externalTaskService, Integer temperatura) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("processo", externalTask.getProcessInstanceId());
        myMap.put("temperatura", temperatura);
        logger.info("Verificando temperatura {}", entries(myMap));

        VariableMap variables = Variables.createVariables();
        variables.putValue("temperatura", temperatura);

        externalTaskService.complete(externalTask, variables);
    }

}