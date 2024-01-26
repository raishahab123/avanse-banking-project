package com.avanse.springboot.controller.globalPages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.avanse.springboot.DTO.forms.contactUs.CustomerDTO;
import com.avanse.springboot.DTO.forms.contactUs.InstituteDTO;
import com.avanse.springboot.DTO.forms.contactUs.InvestorDTO;
import com.avanse.springboot.DTO.forms.contactUs.MediaDTO;
import com.avanse.springboot.DTO.forms.contactUs.AssociateWithUsDTO;
import com.avanse.springboot.model.forms.contactUs.Customer;
import com.avanse.springboot.model.forms.contactUs.Institute;
import com.avanse.springboot.model.forms.contactUs.Investor;
import com.avanse.springboot.model.forms.contactUs.Media;
import com.avanse.springboot.model.forms.contactUs.AssociateWithUs;
import com.avanse.springboot.service.forms.contactUs.CustomerService;
import com.avanse.springboot.service.forms.contactUs.InstituteService;
import com.avanse.springboot.service.forms.contactUs.InvestorService;
import com.avanse.springboot.service.forms.contactUs.MediaService;
import com.avanse.springboot.service.forms.contactUs.AssociateWithUsService;
@Controller
public class ContactUsController {
	@Autowired
	CustomerService customerService;
	
	@Autowired
	InstituteService instituteService;
	
	@Autowired
	InvestorService investorService;
	
	@Autowired
	MediaService mediaService;

	@Autowired
	AssociateWithUsService associateWithUsService;
	
	
	@GetMapping("/admin/contactUs/customers")
	public String getFirstCustomersPage(Model model) {

//		List<University>universities = universityService.getAllUniversity();
//		model.addAttribute("universities", universityService.getAllUniversity());

		return listCustomersByPage(1, model);
	}
	
	@GetMapping("/admin/contactUs/customers/page/{pageNum}")
	public String listCustomersByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Customer> page = customerService.listCustomersByPage(pageNum);
		List<Customer> customers = page.getContent();
		
		/*
		 * //System.out.println("PageNum =" + pageNum);
		 * //System.out.println("Total elements= "+page.getNumberOfElements());
		 * //System.out.println("Total Pages= "+page.getTotalPages());
		 */
				
		long startCount = (pageNum - 1) * customerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + customerService.CUSTOMERS_PER_PAGE- 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("customers", customers);
		return "customersPage";
	}
	
	
		
	
	@GetMapping("/admin/contactUs/institutes")
	public String getFirstInstitutesPage(Model model) {
		return listInstitutesByPage(1, model);
	}
	
	@GetMapping("/admin/contactUs/institutes/page/{pageNum}")
	public String listInstitutesByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Institute> page = instituteService.listInstituteByPage(pageNum);
		List<Institute> institutes= page.getContent();
		
		
		long startCount = (pageNum - 1) * instituteService.INSTITUTES_PER_PAGE + 1;
		long endCount = startCount + instituteService.INSTITUTES_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("institutes", institutes);
		return "institutes";
	}
	
	
	@GetMapping("/admin/contactUs/investors")
	public String getFirstInvestorsPage(Model model) {
		return listInvestorsByPage(1, model);
	}
	

	@GetMapping("/admin/contactUs/investors/page/{pageNum}")
	public String listInvestorsByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Investor> page = investorService.listInvestorsByPage(pageNum);
		List<Investor> investors= page.getContent();
	
		long startCount = (pageNum - 1) * investorService.INVESTORS_PER_PAGE + 1;
		long endCount = startCount + investorService.INVESTORS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("investors", investors);
		return "investors";
	}
	
	@GetMapping("/admin/contactUs/mediaLeads")
	public String getFirstMediaLeadsPage(Model model) {
		return listMediaLeadsByPage(1, model);
		
	}
	
	@GetMapping("/admin/contactUs/mediaLeads/page/{pageNum}")
	public String listMediaLeadsByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
		Page<Media> page = mediaService.listMediaLeadsByPage(pageNum);
		List<Media> mediaLeads= page.getContent();
		
		long startCount = (pageNum - 1) * mediaService.MEDIA_LEADS_PER_PAGE + 1;
		long endCount = startCount + mediaService.MEDIA_LEADS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", startCount);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("mediaLeads", mediaLeads);
		return "media";
	}

	
	
	
	
	
	
	/*
	 * Common post add method for all forms
	*/
	@PostMapping("/contact-us/thankyou")
	public String customersAddPost(@ModelAttribute("customerDTO") CustomerDTO customerDTO,
									@ModelAttribute("investorDTO") InvestorDTO investorDTO,
									@ModelAttribute("instituteDTO") InstituteDTO instituteDTO,
									@ModelAttribute ("mediaDTO") MediaDTO mediaDTO,
								   @ModelAttribute ("associateWithUsDTO") AssociateWithUsDTO associateWithUsDTO,
									@RequestParam("leadType") String leadType) {
		
		/*
		 * //System.out.println(instituteDTO.toString());
		 * //System.out.println(investorDTO.toString());
		 * //System.out.println(mediaDTO.toString());
		 */
		switch (leadType) {
		
		case "customer" :
			Customer customer = new Customer();
			customer.setName(customerDTO.getName());
			customer.setCity(customerDTO.getCity());
			customer.setEmail(customerDTO.getEmail());
			customer.setPhoneNumber(customerDTO.getPhoneNumber());
			customer.setLoanAccountNumber(customerDTO.getLoanAccountNumber());
			customer.setLoanStatus(customerDTO.getLoanStatus());
			customerService.addCustomer(customer);
			break;
			
		case "investor" :
			Investor investor = new Investor();
			
			investor.setId(investorDTO.getId());
			investor.setName(investorDTO.getName());
			investor.setCity(investorDTO.getCity());
			investor.setEmailId(investorDTO.getEmailId());
			investor.setPhoneNumber(investorDTO.getPhoneNumber());
			investor.setSubjectLine(investorDTO.getSubjectLine());
			investorService.addInvestor(investor);
						
			break;
			
		case "institute" :
			Institute institute = new Institute();
			institute.setNameOfPerson(instituteDTO.getNameOfPerson());
			institute.setNameOfInstitute(instituteDTO.getNameOfInstitute());
			institute.setPhoneNumber(instituteDTO.getPhoneNumber());
			institute.setEmail(instituteDTO.getEmail());
			institute.setCity(instituteDTO.getCity());
			institute.setSubjectLine(instituteDTO.getSubjectLine());
			institute.setLoanType(instituteDTO.getLoanType());
			instituteService.addInstitute(institute);
			
			break;
			case "associateWithUS" :
				AssociateWithUs associate = new AssociateWithUs();
				associate.setName(associateWithUsDTO.getName());
				associate.setEmail(associateWithUsDTO.getEmail());
				associate.setContactNumber(associateWithUsDTO.getContactNumber());
				associate.setCity(associateWithUsDTO.getCity());
				associate.setPartnershipType(associateWithUsDTO.getPartnershipType());
				associateWithUsService.addAssociateWithUsLead(associate);

				break;

			
		case "media":
			Media media = new Media();
			media.setId(mediaDTO.getId());
			media.setName(mediaDTO.getName());
			media.setMediaHouse(mediaDTO.getMediaHouse());
			media.setPhoneNumber(mediaDTO.getMediaHouse());
			media.setEmailId(mediaDTO.getEmailId());
			media.setCity(mediaDTO.getCity());
			media.setSubjectLine(mediaDTO.getSubjectLine());
			mediaService.addMedia(media);

			break;
			
		default: 
//			//System.out.println("Couldn't find the lead Type");
		
		}

		return "dynamicPages/thankyou";
	}

	
	
	
	
}
