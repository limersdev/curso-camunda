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
import java.util.logging.Level;

import static net.logstash.logback.argument.StructuredArguments.entries;

@Component
@ExternalTaskSubscription("higrometro")
public class Handler implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        Integer umidade = null;
        try {
            umidade = restTemplate.getForObject("http://wiremock:8080/umidade", Integer.class);

            processarTemperatura(externalTask, externalTaskService, umidade);

        } catch (Exception e) {
            externalTaskService.handleFailure(externalTask, "Erro ao consultar temperatura", e.getMessage(), 0, 0L);
        }
    }

    private void processarTemperatura(ExternalTask externalTask, ExternalTaskService externalTaskService, Integer umidade) {
        Map<String, Object> myMap = new HashMap<>();
        myMap.put("processo", externalTask.getProcessInstanceId());
        myMap.put("umidade", umidade);
        logger.info("Verificando umidade {}", entries(myMap));

        VariableMap variables = Variables.createVariables();
        variables.putValue("umidade", umidade);

        externalTaskService.complete(externalTask, variables);
    }

}