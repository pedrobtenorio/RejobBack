package com.efjpr.rejob.controller;

import com.efjpr.rejob.service.JobRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/test")
public class testController {

    @GetMapping
    public ResponseEntity<String> test()
    {
        return ResponseEntity.ok("Secured endpoint uhu!!");
    }

    @GetMapping("stem")
    public ResponseEntity<Double> stemTest()
    {
        return ResponseEntity.ok(JobRecommendationService.calculateJaccardSimilarity("Desenvolvedor Full Stack Sênior\n" +
                        "\n" +
                        "Requisitos:\n" +
                        "Experiência comprovada de pelo menos dez anos como desenvolvedor full stack.\n" +
                        "Proficiência avançada em Java, JavaScript, React e Node.js.\n" +
                        "Histórico sólido no desenvolvimento de aplicativos web e móveis.\n" +
                        "Experiência em liderar equipes técnicas em projetos complexos.\n" +
                        "Conhecimento profundo em metodologias ágeis.\n" +
                        "Habilidade comprovada em treinar e orientar membros da equipe.\n" +
                        "Familiaridade com arquiteturas escaláveis e boas práticas de segurança.\n" +
                        "Excelentes habilidades de comunicação e resolução de problemas.\n",
                        "Trabalhei como desenvolvedor full stack em diversas empresas ao longo dos últimos dez anos. Durante esse período, acumulei uma sólida experiência no desenvolvimento e implementação de soluções inovadoras em tecnologias como Java, JavaScript, React e Node.js. Liderei equipes técnicas em projetos desafiadores, garantindo a entrega de soluções de alta qualidade. Participei ativamente de workshops técnicos, treinando e orientando novos membros da equipe. Minhas habilidades de comunicação e resolução de problemas foram aprimoradas ao lidar com prazos apertados e ambientes de trabalho dinâmicos. Estou familiarizado com metodologias ágeis e comprometido com a excelência no desenvolvimento de software.\n"));
    }
}
