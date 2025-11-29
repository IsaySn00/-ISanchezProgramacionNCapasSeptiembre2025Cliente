package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.Controller;

import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Colonia;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Direccion;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.ErrorCarga;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Estado;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Municipio;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Pais;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Result;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Rol;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private static final String urlBase = "http://localhost:8080";

    @GetMapping("formularioUsuario")
    public String FormularioUsuario() {
        return "UsuarioForm";
    }

    @GetMapping("indexUsuario")
    public String Index(Model model) {

        model.addAttribute("Usuario", new Usuario());

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "/api/usuarios/usuario",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuarios", result.object);

        } else {
            return "Error";
        }

        return "UsuarioIndex";
    }

    @PostMapping("indexUsuario")
    public String Index(@ModelAttribute("Usuario") Usuario usuario, Model model) {

        model.addAttribute("Usuario", usuario);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);

        ResponseEntity<Result<List<Usuario>>> responseEntity
                = restTemplate.exchange(urlBase + "/api/usuarios/GetUsuariosDinamico",
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuarios", result.object);
        } else {
            return "Error";
        }

        return "UsuarioIndex";
    }

    @GetMapping("login")
    public String Login() {
        return "login";
    }

    @PostMapping("login")
    public String Login(@RequestParam("username") String username, @RequestParam("password") String password, Model model, HttpSession session) {

        try {
            Usuario usuario = new Usuario();
            usuario.setUserName(username);
            usuario.setPasswordUser(password);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders jsonHeaders = new HttpHeaders();

            jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, jsonHeaders);

            ResponseEntity<Result<Map<String, Object>>> responseEntity = restTemplate.exchange(urlBase + "/api/auth/login",
                    HttpMethod.POST,
                    usuarioEntity,
                    new ParameterizedTypeReference<Result<Map<String, Object>>>() {
            });

            Map<String, Object> data = responseEntity.getBody().object;

            String rol = data.get("rol").toString();
            Integer idUsuario = (Integer) data.get("idUsuario");
            String tkn = data.get("token").toString();

            session.setAttribute("tkn", tkn);

            if (rol.equals("admin")) {
                return "redirect:/usuario/indexUsuario";
            } else if (rol.equals("usuario")) {
                return "redirect:/usuario/detail/" + idUsuario;
            }
        } catch (Exception ex) {
            model.addAttribute("loginError", true);
            return "redirect:/usuario/login";
        }

        model.addAttribute("loginError", true);
        return "login";

    }

    @GetMapping("cargaMasiva")
    public String CargaMasiva() {
        return "UsuarioCargaMasiva";
    }

    @GetMapping("cargaMasiva/procesar")
    public String CargaMasiva(HttpSession session, Model model) {

        String tkn = session.getAttribute("tkn").toString();
        session.removeAttribute("tkn");

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "/api/usuarios/cargaMasiva?tkn=" + tkn,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                Result.class);

        if (responseEntity.getStatusCode().value() == 201) {
            model.addAttribute("success", "Los usuarios se procesaron con exito");
            model.addAttribute("icon", "success");
        } else {
            model.addAttribute("success", "El archivo no se ha podido procesar");
            model.addAttribute("icon", "error");
        }

        return "UsuarioCargaMasiva";
    }

    @PostMapping("cargaMasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession session) {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("archivo", archivo.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "/api/usuarios/cargaMasiva",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class);

            model.addAttribute("successValidation", true);
            session.setAttribute("tkn", responseEntity.getBody().object);

        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            ObjectMapper mapper = new ObjectMapper();

            Result result = new Result();

            try {
                result = mapper.readValue(ex.getResponseBodyAsString(), Result.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("errores", result.object);
        }

        return "UsuarioCargaMasiva";

    }

    @GetMapping("detail/{idUsuario}")
    public String Detail(@PathVariable("idUsuario") int idUsuario, Model model, HttpSession session) {

        String token = (String) session.getAttribute("tkn");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuarios/usuario/" + idUsuario,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuario", result.object);
        }

        model.addAttribute("direccion", new Direccion());
        model.addAttribute("roles", GetRoles().object);
        model.addAttribute("paises", GetPaises().object);
//        if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//            model.addAttribute("estados", estadoJPADAOImplementation.GetAll(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);
//            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//                model.addAttribute("municipios", municipioJPaDAOImplementation.GetAllMunicipioByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);
//                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
//                    model.addAttribute("colonias", coloniaJPADAOImplementation.GetAllColoniaByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
//                }
//            }
//        }

        return "UsuarioDetail";
    }
//

    @GetMapping("add")
    public String AddUsuarioView(Model model) {

        Usuario usuario = new Usuario();

        model.addAttribute("Usuario", usuario);

        model.addAttribute("paises", GetPaises().object);

        model.addAttribute("roles", GetRoles().object);

        return "UsuarioForm";
    }

    public Result GetPaises() {
        RestTemplate restTemplate = new RestTemplate();

        Result resultPais = new Result();

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                urlBase + "/api/pais/paises",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntityPais.getStatusCode().value() == 200) {
            resultPais = responseEntityPais.getBody();
        }

        return resultPais;
    }

    public Result GetEstados(int idPais) {
        RestTemplate restTemplate = new RestTemplate();

        Result result = new Result();

        ResponseEntity<Result<List<Estado>>> responseEntity = restTemplate.exchange(
                urlBase + "/api/estado/estados/" + idPais,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Estado>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    public Result GetMunicipios(int idEstado) {
        RestTemplate restTemplate = new RestTemplate();

        Result result = new Result();

        ResponseEntity<Result<List<Municipio>>> responseEntity = restTemplate.exchange(
                urlBase + "/api/municipio/municipios/" + idEstado,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Municipio>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    public Result GetColonias(int idColonia) {
        RestTemplate restTemplate = new RestTemplate();

        Result result = new Result();

        ResponseEntity<Result<List<Colonia>>> responseEntity = restTemplate.exchange(
                urlBase + "/api/colonia/colonias/" + idColonia,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Colonia>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            result = responseEntity.getBody();
        }

        return result;
    }

    public Result GetRoles() {
        RestTemplate restTemplate = new RestTemplate();

        Result resultRol = new Result();

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                urlBase + "/api/rol/roles",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        if (responseEntityRol.getStatusCode().value() == 200) {
            resultRol = responseEntityRol.getBody();
        }

        return resultRol;
    }

    @PostMapping("add")
    public String addUsuario(@Valid @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            Model model, @RequestParam("imagenFile") MultipartFile imagenFile) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();

        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, jsonHeaders);

        body.add("usuario", usuarioEntity);

        if (imagenFile != null) {
            try {
                ByteArrayResource fileAsResource = new ByteArrayResource(imagenFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        return imagenFile.getOriginalFilename();
                    }
                };

                HttpHeaders fileHeaders = new HttpHeaders();
                fileHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                body.add("imagenFile", new HttpEntity<>(fileAsResource, fileHeaders));
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(
                    urlBase + "/api/usuarios/usuario",
                    HttpMethod.POST,
                    requestEntity,
                    Result.class);

            redirectAttributes.addFlashAttribute("success", "EL usuario" + usuario.getUserName() + "Se creo con exito");
            redirectAttributes.addFlashAttribute("icon", "success");

        } catch (HttpClientErrorException.UnprocessableEntity ex) {
            ObjectMapper mapper = new ObjectMapper();

            Result result = new Result();

            try {
                result = mapper.readValue(ex.getResponseBodyAsString(), Result.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            model.addAttribute("error", result.object);

            model.addAttribute("Usuario", usuario);
            model.addAttribute("roles", GetRoles().object);
            model.addAttribute("paises", GetPaises().object);
            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
                model.addAttribute("estados", GetEstados(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).object);
                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
                    model.addAttribute("municipios", GetMunicipios(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).object);
                    if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
                        model.addAttribute("colonias", GetColonias(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).object);
                    }
                }
            }
            return "UsuarioForm";

        }

//        if (responseEntity.getStatusCode().value() == 201) {
//
//        } else if (responseEntity.getStatusCode().value() == 422) {
//            
//        } else {
//            redirectAttributes.addFlashAttribute("success", "EL usuario no se ha podido crear");
//            redirectAttributes.addFlashAttribute("icon", "error");
//        }
        return "redirect:/usuario/indexUsuario";
    }

    @PutMapping("/detail")
    public String updateUsuario(@ModelAttribute("usuario") Usuario usuario, Model model,
            RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> request = new HttpEntity<>(usuario, httpHeaders);

        try {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "/api/usuarios/updateUsuario",
                    HttpMethod.PUT,
                    request,
                    Result.class);

            redirectAttributes.addFlashAttribute("successMessage", "EL usuario editó con exito");
            redirectAttributes.addFlashAttribute("iconModal", "success");

        } catch (HttpClientErrorException.UnprocessableEntity ex) {

            ObjectMapper mapper = new ObjectMapper();

            Result result = new Result();

            try {
                result = mapper.readValue(ex.getResponseBodyAsString(), Result.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            redirectAttributes.addFlashAttribute("error", result.object);

            redirectAttributes.addFlashAttribute("usuario", usuario);

            redirectAttributes.addFlashAttribute("roles", GetRoles().object);

            return "redirect:/usuario/detail/" + usuario.getIdUsuario();
        }

//
//        if (responseEntity.getStatusCode().value() == 202) {
//
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "EL usuario no se ha podido editar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
    }

    @PatchMapping("updateImgUsuario")
    public String UpdateImgUsuario(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirectAttributes,
            @RequestParam("imagenFile") MultipartFile imagenFile) {

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();

        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> usuarioEntity = new HttpEntity<>(usuario, jsonHeaders);

        body.add("usuario", usuarioEntity);

        if (imagenFile != null) {
            try {
                ByteArrayResource fileAsResource = new ByteArrayResource(imagenFile.getBytes()) {
                    @Override
                    public String getFilename() {
                        return imagenFile.getOriginalFilename();
                    }
                };

                HttpHeaders fileHeaders = new HttpHeaders();
                fileHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                body.add("imgFile", new HttpEntity<>(fileAsResource, fileHeaders));
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Result> responseEntity = restTemplate.exchange(
                urlBase + "/api/usuarios/updateImgUsuario",
                HttpMethod.PATCH,
                requestEntity,
                Result.class);

        if (responseEntity.getStatusCode().value() == 202) {
            redirectAttributes.addFlashAttribute("successMessage", "La imagen se actualizó con exito");
            redirectAttributes.addFlashAttribute("iconModal", "success");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "La imagen no se pudó actualizar");
            redirectAttributes.addFlashAttribute("iconModal", "error");
        }

        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
    }
//
//    @PostMapping("deleteUsuario/{idUsuario}")
//    public String deleteUsuario(@PathVariable("idUsuario") int idUsuario, RedirectAttributes redirectAttributes) {
//        Result result = usuarioJPADAOImplementation.DeleteUsuario(idUsuario);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "EL usuario editó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "EL usuario no se ha podido editar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/indexUsuario";
//    }

    @PostMapping("actionDireccion/{idUsuario}")
    public String ActionDireccion(@ModelAttribute("Direccion") Direccion direccion,
            @PathVariable int idUsuario,
            RedirectAttributes redirectAttributes) {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        HttpHeaders jsonHeaders = new HttpHeaders();

        jsonHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Direccion> direccionEntity = new HttpEntity<>(direccion, jsonHeaders);

        body.add("direccion", direccionEntity);

        if (direccion.getIdDireccion() > 0) {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "/api/direcciones/direccion?idUsuario=" + idUsuario,
                    HttpMethod.PUT,
                    direccionEntity,
                    Result.class);

            if (responseEntity.getStatusCode().value() == 202) {
                redirectAttributes.addFlashAttribute("successMessage", "La dirección se actualizó con éxito");
                redirectAttributes.addFlashAttribute("iconModal", "success");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "La dirección no se pudo actualizar");
                redirectAttributes.addFlashAttribute("iconModal", "error");
            }
        } else {
            ResponseEntity<Result> responseEntity = restTemplate.exchange(urlBase + "/api/direcciones/direccion?idUsuario=" + idUsuario,
                    HttpMethod.POST,
                    direccionEntity,
                    Result.class);

            if (responseEntity.getStatusCode().value() == 201) {
                redirectAttributes.addFlashAttribute("successMessage", "La dirección se agregó con éxito");
                redirectAttributes.addFlashAttribute("iconModal", "success");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "La dirección no se pudo agregar");
                redirectAttributes.addFlashAttribute("iconModal", "error");
            }
        }

        return "redirect:/usuario/detail/" + idUsuario;
    }
}
