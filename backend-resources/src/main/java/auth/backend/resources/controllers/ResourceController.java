package auth.backend.resources.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class ResourceController {

    private static final GrantedAuthority GOLD_CUSTOMER = new SimpleGrantedAuthority("gold");

    String[] elementArray = {"","Hydrogen","Helium","Lithium","Beryllium","Boron","Carbon","Nitrogen","Oxygen","Fluorine","Neon","Sodium","Magnesium","Aluminum","Silicon","Phosphorus","Sulfur","Chlorine","Argon","Potassium","Calcium","Scandium","Titanium","Vanadium","Chromium","Manganese","Iron","Cobalt","Nickel","Copper","Zinc","Gallium","Germanium","Arsenic","Selenium","Bromine","Krypton","Rubidium","Strontium","Yttrium","Zirconium","Niobium","Molybdenum","Technetium","Ruthenium","Rhodium","Palladium","Silver","Cadmium","Indium","Tin","Antimony","Tellurium","Iodine","Xenon","Cesium","Barium","Lanthanum","Cerium","Praseodymium","Neodymium","Promethium","Samarium","Europium","Gadolinium","Terbium","Dysprosium","Holmium","Erbium","Thulium","Ytterbium","Lutetium","Hafnium","Tantalum","Tungsten","Rhenium","Osmium","Iridium","Platinum","Gold","Mercury","Thallium","Lead","Bismuth","Polonium","Astatine","Radon","Francium","Radium","Actinium","Thorium","Protactinium","Uranium","Neptunium","Plutonium","Americium","Curium","Berkelium","Californium","Einsteinium","Fermium","Mendelevium","Nobelium","Lawrencium","Rutherfordium","Dubnium","Seaborgium","Bohrium","Hassium","Meitnerium","Darmstadtium","Roentgenium","","Ununbiium",""};
    List<String> elements = new ArrayList<>(List.of(elementArray));

    @GetMapping("/messages")
//    public String getMessages(JwtAuthenticationToken auth, @AuthenticationPrincipal Jwt princial){
    public String getMessages(@AuthenticationPrincipal Jwt principal){
        log.info("Claims is " + principal.getClaims().toString());
        return "the protected messages";
    }

    @GetMapping("/user/info")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        log.info("getUserInfo called for user " + principal.getClaimAsString("preferred_username"));
        Map<String, String> map = new Hashtable<>();
        map.put("user_name", principal.getClaimAsString("preferred_username"));
        map.put("organization", principal.getClaimAsString("organization"));
        return Collections.unmodifiableMap(map);
    }

    @PostMapping("/post/message")
    public String postMessage(@RequestBody String message) {

        return "Posted: " + message;
    }
    @GetMapping(path= "/rose")
    public String rose(@AuthenticationPrincipal Jwt principal){
        return "--------{---(@";
    }
    
    @GetMapping(value = "/element/{num}")
    public ResponseEntity<String> elementGet(@PathVariable String num) throws JsonProcessingException {
        Map<String,String> result = new HashMap<>();
        try {
            result.put(num, elements.get(Integer.parseInt(num)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(result);
        return ResponseEntity.ok(json);
    }

    @PostMapping(value = "/addElement")
    public ResponseEntity<String> elementPost(@RequestBody String newElement) throws JsonProcessingException {
        Map<String,String> result = new HashMap<>();
        try {
            String[] element = newElement.split(" ");
            int atomicNum = Integer.parseInt(element[0]);

            elements.set(atomicNum, element[1]);
            result.put(element[0], elements.get(atomicNum));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(result);
        return ResponseEntity.ok(json);
    }
}
