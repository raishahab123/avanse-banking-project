package com.avanse.springboot.controller.globalPages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.avanse.springboot.DTO.JobResponseDTO;
import com.avanse.springboot.DTO.JobSearchResponseDTO;
import com.avanse.springboot.repository.JobRespository;
import com.avanse.springboot.service.JobService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CareerController {
	public static final String GET_ALL_JOBS = "https://api.turbohire.co/api/careerpagejobs";

	static RestTemplate restTemplate = new RestTemplate();
	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobRespository jobRespository;
	
	public static void main(String[] args)
	{
		new CareerController().callGetAllJobsAPI();
	}
//	This was just an attempt will have to work on it


	 @GetMapping("/public/api/getAllJobs")
	 public List<JobResponseDTO> getAllJobs(@RequestParam(name="city",defaultValue = "mumbai")String city ) {
		  System.out.println("====Getting job====" + city ); // return
		  //return jobService.getAllJobs();
		  List<JobResponseDTO> filteredJobs = new ArrayList();
		  filteredJobs = callGetAllJobsAPI().getResult().stream()
				  .filter(a -> a.getLocation().toLowerCase().contains(city)).collect(Collectors.toList());
		  return filteredJobs;
	  
	 }
	 
	  //@CrossOrigin(origins = "*")
	private JobSearchResponseDTO callGetAllJobsAPI()  {
		HttpHeaders headers = new HttpHeaders();
		//headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		  headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("x-api-key", "C6DD8660-4792-427D-A7A1-A93DCFEF5094");

		HttpEntity<String> entity=new HttpEntity<>("parameters",headers);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
//		ResponseEntity<JobSearchResponseDTO> result = restTemplate.postForEntity(GET_ALL_JOBS, entity, JobSearchResponseDTO.class);
		ResponseEntity<String> result = restTemplate.exchange(GET_ALL_JOBS, HttpMethod.POST, entity, String.class);
//		System.out.println(result.getBody());
		JobSearchResponseDTO searchResponse = null;
		try {
			System.out.println(result.getBody());
			searchResponse = objectMapper.readValue(result.getBody(), JobSearchResponseDTO.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		System.out.println(searchResponse);
		return searchResponse;
	  }
	  
	
}