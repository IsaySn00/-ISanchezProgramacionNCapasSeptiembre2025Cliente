package com.digis01.ISanchezProgramacionNCapasSeptiembre2025.Controller;

import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Colonia;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.Direccion;
import com.digis01.ISanchezProgramacionNCapasSeptiembre2025.ML.ErrorCarga;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
//
//    @GetMapping("cargaMasiva")
//    public String CargaMasiva() {
//        return "UsuarioCargaMasiva";
//    }
//
//    @GetMapping("cargaMasiva/procesar")
//    public String CargaMasiva(HttpSession session, Model model) {
//
//        String path = session.getAttribute("archivoCargaMasiva").toString();
//        session.removeAttribute("archivoCargaMasiva");
//        List<Usuario> lista = new ArrayList<>();
//
//        File file = new File(path);
//        String extension = FilenameUtils.getExtension(path);
//
//        if (extension.equals("txt")) {
//            lista = LeerArchivoTXT(file);
//        } else if (extension.equals("xlsx")) {
//            lista = LecturaArchivoXLSX(file);
//        }
//
//        Result result = usuarioJPADAOImplementation.AddUsuariosByFile(lista);
//
//        if (result.correct) {
//            model.addAttribute("success", "Los usuarios se procesaron con exito");
//            model.addAttribute("icon", "success");
//        } else {
//            model.addAttribute("success", "El archivo no se ha podido procesar");
//            model.addAttribute("icon", "error");
//        }
//
//        return "UsuarioCargaMasiva";
//    }
//
//    @PostMapping("cargaMasiva")
//    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo, Model model, HttpSession session) {
//
//        List<Usuario> lista = new ArrayList<>();
//        List<ErrorCarga> listaError = new ArrayList<>();
//
//        String extension = archivo.getOriginalFilename().split("\\.")[1];
//
//        String path = System.getProperty("user.dir");
//        String pathArchivo = "src/main/resources/archivosCarga";
//        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//        String pathDefinitivo = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();
//
//        try {
//            archivo.transferTo(new File(pathDefinitivo));
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalStateException ex) {
//            java.util.logging.Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        File file = new File(pathDefinitivo);
//
//        if (extension.equals("txt")) {
//            lista = LeerArchivoTXT(file);
//        } else if (extension.equals("xlsx")) {
//            lista = LecturaArchivoXLSX(file);
//        } else {
//
//        }
//
//        listaError = validarDatosArchivo(lista);
//
//        if (listaError.size() > 0) {
//            model.addAttribute("errores", listaError);
//        } else {
//            model.addAttribute("successValidation", true);
//            session.setAttribute("archivoCargaMasiva", pathDefinitivo);
//        }
//
//        return "UsuarioCargaMasiva";
//
//    }
//
//    public List<Usuario> LeerArchivoTXT(File archivo) {
//        List<Usuario> usuarioList = new ArrayList<>();
//
//        try (InputStream inputStream = new FileInputStream(archivo); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
//            String linea = "";
//
//            while ((linea = bufferedReader.readLine()) != null) {
//
//                String[] campos = linea.split("\\|");
//                Usuario usuario = new Usuario();
//                usuario.setNombreUsuario(campos[0]);
//                usuario.setApellidoPatUsuario(campos[1]);
//                usuario.setApellidoMatUsuario(campos[2]);
//                usuario.setPasswordUser(campos[3]);
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                Date fechaNac = formatter.parse(campos[4]);
//                usuario.setFechaNacimiento(fechaNac);
//                usuario.setStatusUsuario(campos[5]);
//                usuario.setFechaModificacion(new Date());
//                usuario.setUserName(campos[7]);
//                usuario.setEmailUsuario(campos[8]);
//                usuario.setSexoUsuario(campos[9]);
//                usuario.setTelefonoUsuario(campos[10]);
//                usuario.setCelularUsuario(campos[11]);
//                usuario.setCurpUsuario(campos[12]);
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(Integer.parseInt(campos[13]));
//
//                usuarioList.add(usuario);
//            }
//
//        } catch (Exception ex) {
//            return null;
//        }
//        return usuarioList;
//    }
//
//    public List<Usuario> LecturaArchivoXLSX(File archivo) {
//        List<Usuario> usuarioList = new ArrayList<>();
//
//        try (FileInputStream fis = new FileInputStream(archivo); XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
//            XSSFSheet workSheet = workbook.getSheetAt(0);
//            for (Row row : workSheet) {
//                Usuario usuario = new Usuario();
//                usuario.setNombreUsuario(row.getCell(0).toString());
//                usuario.setApellidoPatUsuario(row.getCell(1).toString());
//                usuario.setApellidoMatUsuario(row.getCell(2).toString());
//                usuario.setPasswordUser(row.getCell(3).toString());
//
//                Date fechaNac = null;
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
//                if (row.getCell(4) != null) {
//                    switch (row.getCell(4).getCellType()) {
//                        case NUMERIC:
//                            if (DateUtil.isCellDateFormatted(row.getCell(4))) {
//                                fechaNac = row.getCell(4).getDateCellValue();
//                            } else {
//                                fechaNac = new Date((long) row.getCell(4).getNumericCellValue());
//                            }
//                            break;
//                        case STRING:
//                            fechaNac = formatter.parse(row.getCell(4).getStringCellValue());
//                            break;
//                        default:
//                            fechaNac = null;
//                            break;
//                    }
//                }
//
//                usuario.setFechaNacimiento(fechaNac);
//                usuario.setStatusUsuario(row.getCell(5).toString());
//                usuario.setFechaModificacion(new Date());
//                usuario.setUserName(row.getCell(7).toString());
//                usuario.setEmailUsuario(row.getCell(8).toString());
//                usuario.setSexoUsuario(row.getCell(9).toString());
//                usuario.setTelefonoUsuario(row.getCell(10).toString());
//                usuario.setCelularUsuario(row.getCell(11).toString());
//                usuario.setCurpUsuario(row.getCell(12).toString());
//                usuario.Rol = new Rol();
//                if (row.getCell(13) != null) {
//                    if (row.getCell(13).getCellType() == CellType.NUMERIC) {
//                        usuario.Rol.setIdRol((int) row.getCell(13).getNumericCellValue());
//                    } else {
//                        usuario.Rol.setIdRol(Integer.parseInt(row.getCell(13).getStringCellValue().trim()));
//                    }
//                }
//                usuarioList.add(usuario);
//            }
//
//        } catch (Exception ex) {
//            return null;
//        }
//        return usuarioList;
//
//    }
//
//    public List<ErrorCarga> validarDatosArchivo(List<Usuario> lista) {
//
//        List<ErrorCarga> errorLista = new ArrayList<>();
//
//        String validarLetras = "^[a-zA-ZÁÉÍÓÚáéíóúñÑ\\s]+$";
//        Pattern patternLetras = Pattern.compile(validarLetras);
//
//        String validarCorreo = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
//        Pattern patternCorreo = Pattern.compile(validarCorreo);
//
//        String validarUsername = "^[a-zA-Z0-9_]+$";
//        Pattern patternUsername = Pattern.compile(validarUsername);
//
//        //Validaciones de contraseña
//        String RegExpMayus = "[A-Z]";
//        Pattern patternMayus = Pattern.compile(RegExpMayus);
//
//        String RegExpNumber = "[\\d]";
//        Pattern patternNumber = Pattern.compile(RegExpNumber);
//
//        String RegExpEspChar = "[@$?#¡!\\-_]";
//        Pattern patternEspChar = Pattern.compile(RegExpEspChar);
//
//        String RegExpMinChar = ".{8,}";
//        Pattern patternExpMinChar = Pattern.compile(RegExpMinChar);
//
//        for (int i = 0; i < lista.size(); i++) {
//
//            String nombreUsuario = lista.get(i).getNombreUsuario();
//            Matcher matcherNombre = patternLetras.matcher(nombreUsuario);
//
//            String apellidoPatUsuario = lista.get(i).getApellidoPatUsuario();
//            Matcher matcherApellidoPat = patternLetras.matcher(apellidoPatUsuario);
//
//            String apellidoMatUsuario = lista.get(i).getApellidoMatUsuario();
//            Matcher matcherApellidoMat = patternLetras.matcher(apellidoMatUsuario);
//
//            Date fechaNac = lista.get(i).getFechaNacimiento();
//            Date fechaAct = new Date();
//            String passwordUsuario = lista.get(i).getPasswordUser();
//            Matcher matcherMayus = patternMayus.matcher(passwordUsuario);
//            Matcher matcherNumber = patternNumber.matcher(passwordUsuario);
//            Matcher matcherEspChar = patternEspChar.matcher(passwordUsuario);
//            Matcher matcherMinChar = patternExpMinChar.matcher(passwordUsuario);
//
//            String userName = lista.get(i).getUserName();
//            Matcher matcherUsername = patternUsername.matcher(userName);
//
//            String email = lista.get(i).getEmailUsuario();
//            Matcher matcherEmail = patternCorreo.matcher(email);
//
//            if (!matcherNombre.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Nombre";
//                errorCarga.descripcion = "El campo solo permite letras";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherApellidoPat.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Apellido Paterno";
//                errorCarga.descripcion = "El campo solo permite letras";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherApellidoMat.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Apellido Materno";
//                errorCarga.descripcion = "El campo solo permite letras";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherMayus.find()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Contraseña";
//                errorCarga.descripcion = "La contraseña tiene que tiener al menos una letra mayúscula";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherNumber.find()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Contraseña";
//                errorCarga.descripcion = "La contraseña tiene que tiener al menos un numero";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherEspChar.find()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Contraseña";
//                errorCarga.descripcion = "La contraseña tiene que tiener al menos un carácter especial";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherMinChar.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Contraseña";
//                errorCarga.descripcion = "La contraseña tiene que tiener al menos 8 carácteres";
//                errorLista.add(errorCarga);
//            }
//
//            if (fechaNac.after(fechaAct)) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Fecha de Nacimiento";
//                errorCarga.descripcion = "La fecha de nacimiento no es valida";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherUsername.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Nombre de usuario";
//                errorCarga.descripcion = "El nombre de usuario no es valido";
//                errorLista.add(errorCarga);
//            }
//
//            if (!matcherEmail.matches()) {
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.linea = i + 1;
//                errorCarga.campo = "Correo electrónico";
//                errorCarga.descripcion = "El correo electrónico no es valido";
//                errorLista.add(errorCarga);
//            }
//        }
//        return errorLista;
//    }
//
//    @GetMapping("detail/{idUsuario}")
//    public String Detail(@PathVariable("idUsuario") int idUsuario, Model model) {
//        Result result = usuarioJPADAOImplementation.GetById(idUsuario);
//
//        Usuario usuario = (Usuario) result.object;
//
//        model.addAttribute("direccion", new Direccion());
//        model.addAttribute("usuario", result.object);
//        model.addAttribute("roles", rolJPADAOImplementation.GetAll().objects);
//        model.addAttribute("paises", paisJPADAOImplementation.GetAll().objects);
//        if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//            model.addAttribute("estados", estadoJPADAOImplementation.GetAll(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);
//            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//                model.addAttribute("municipios", municipioJPaDAOImplementation.GetAllMunicipioByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);
//                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
//                    model.addAttribute("colonias", coloniaJPADAOImplementation.GetAllColoniaByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
//                }
//            }
//        }
//
//        return "UsuarioDetail";
//    }
//

    @GetMapping("add")
    public String AddUsuarioView(Model model) {

        Usuario usuario = new Usuario();

        model.addAttribute("Usuario", usuario);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(
                urlBase + "/api/rol/roles",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(
                urlBase + "/api/pais/paises",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntityRol.getStatusCode().value() == 200 && responseEntityPais.getStatusCode().value() == 200) {
            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("roles", resultRol.object);

            Result resultPais = responseEntityPais.getBody();
            model.addAttribute("paises", resultPais.object);
        }

        return "UsuarioForm";
    }

    @PostMapping("add")
    public String addUsuario(@Valid @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            Model model, @RequestParam("imagenFile") MultipartFile imagenFile) throws JsonProcessingException {

//        if (bindingResult.hasErrors()) {
//            model.addAttribute("Usuario", usuario);
//            model.addAttribute("roles", rolJPADAOImplementation.GetAll().objects);
//            model.addAttribute("paises", paisJPADAOImplementation.GetAll().objects);
//            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//                model.addAttribute("estados", estadoJPADAOImplementation.GetAll(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);
//                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//                    model.addAttribute("municipios", municipioJPaDAOImplementation.GetAllMunicipioByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);
//                    if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
//                        model.addAttribute("colonias", coloniaJPADAOImplementation.GetAllColoniaByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
//                    }
//                }
//            }
//            return "UsuarioForm";
//        }
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

        ResponseEntity<Result> responseEntity = restTemplate.exchange(
                urlBase + "/api/usuarios/usuario",
                HttpMethod.POST,
                requestEntity,
                Result.class);

        if (responseEntity.getStatusCode().value() == 201) {
            redirectAttributes.addFlashAttribute("success", "EL usuario" + usuario.getUserName() + "Se creo con exito");
            redirectAttributes.addFlashAttribute("icon", "success");
        } else {
            redirectAttributes.addFlashAttribute("success", "EL usuario no se ha podido crear");
            redirectAttributes.addFlashAttribute("icon", "error");
        }

        return "redirect:/usuario/indexUsuario";
    }
//
//    @PostMapping("/detail")
//    public String updateUsuario(@ModelAttribute("usuario") Usuario usuario,
//            RedirectAttributes redirectAttributes) {
//
//        Result result = usuarioJPADAOImplementation.UpdateUsuario(usuario);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "EL usuario editó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "EL usuario no se ha podido editar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
//    }
//
//    @PostMapping("updateImgUsuario")
//    public String UpdateImgUsuario(@ModelAttribute("usuario") Usuario usuario,
//            RedirectAttributes redirectAttributes,
//            @RequestParam("imagenFile") MultipartFile imagenFile) {
//
//        if (imagenFile != null) {
//            try {
//                String extension = imagenFile.getOriginalFilename().split("\\.")[1];
//                if (extension.equals("jpg") || extension.equals(("png"))) {
//
//                    byte[] byteImagen = imagenFile.getBytes();
//                    String imagenBase64 = Base64.getEncoder().encodeToString(byteImagen);
//                    usuario.setFotoUsuario(imagenBase64);
//                }
//            } catch (IOException ex) {
//                java.util.logging.Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        Result result = usuarioJPADAOImplementation.UpdateImgUsuario(usuario);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "La imagen se actualizó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "La imagen no se pudó actualizar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + usuario.getIdUsuario();
//    }
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
//
//    @PostMapping("/addDireccion")
//    public String addDireccionToUser(@ModelAttribute("Direccion") Direccion direccion, @RequestParam int usuarioId, RedirectAttributes redirectAttributes) {
//        Result result = direccionJPADAOImplementation.AddDireccion(direccion, usuarioId);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección se agregó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección no se ha podido agregar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + usuarioId;
//    }
//
//    @PostMapping("/updateDireccion")
//    public String updateDireccion(@ModelAttribute("Direccion") Direccion direccion, @RequestParam int usuarioId, RedirectAttributes redirectAttributes) {
//        Result result = direccionJPADAOImplementation.UpdateDireccion(direccion, usuarioId);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección se actualizó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección no se ha podido actualizar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + usuarioId;
//    }
//
//    @PostMapping("actionDireccion/{idUsuario}")
//    public String ActionDireccion(@ModelAttribute("Direccion") Direccion direccion,
//            @PathVariable int idUsuario,
//            RedirectAttributes redirectAttributes) {
//        Result result = new Result();
//
//        if (direccion.getIdDireccion() > 0) {
//            result = direccionJPADAOImplementation.UpdateDireccion(direccion, idUsuario);
//        } else {
//            result = direccionJPADAOImplementation.AddDireccion(direccion, idUsuario);
//        }
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage",
//                    direccion.getIdDireccion() != 0 ? "La dirección se actualizó con éxito" : "La dirección se agregó con éxito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage",
//                    direccion.getIdDireccion() != 0 ? "La dirección no se pudo actualizar" : "La dirección no se pudo agregar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + idUsuario;
//    }
//
//    @PostMapping("/deleteDireccion/{idDireccion}")
//    public String deleteDireccion(@PathVariable("idDireccion") int idDireccion, @RequestParam("usuarioId") int usuarioId, RedirectAttributes redirectAttributes) {
//        Result result = direccionJPADAOImplementation.DeleteDireccion(idDireccion);
//
//        if (result.correct) {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección se eliminó con exito");
//            redirectAttributes.addFlashAttribute("iconModal", "success");
//        } else {
//            redirectAttributes.addFlashAttribute("successMessage", "La dirección no se ha podido eliminar");
//            redirectAttributes.addFlashAttribute("iconModal", "error");
//        }
//
//        return "redirect:/usuario/detail/" + usuarioId;
//    }
//
//    @GetMapping("estado/{idPais}")
//    @ResponseBody
//    public Result GetEstadosById(@PathVariable("idPais") int idPais) {
//        return estadoJPADAOImplementation.GetAll(idPais);
//    }
//
//    @GetMapping("municipio/{idEstado}")
//    @ResponseBody
//    public Result GetMunicipioByIdEstado(@PathVariable("idEstado") int idEstado) {
//        return municipioJPaDAOImplementation.GetAllMunicipioByIdEstado(idEstado);
//    }
//
//    @GetMapping("colonia/{idMunicipio}")
//    @ResponseBody
//    public Result GetColoniaByIdMunicipio(@PathVariable("idMunicipio") int idMunicipio) {
//        return coloniaJPADAOImplementation.GetAllColoniaByIdMunicipio(idMunicipio);
//    }
//
//    @GetMapping("colonia")
//    @ResponseBody
//    public Result GetCodigoPostalByNameColoniaIdMnpio(@RequestParam("nombreColonia") String nombreColonia, @RequestParam("idMunicipio") int idMunicipio) {
//        return coloniaJPADAOImplementation.GetCodigoPostalByNameColoniaIdMnpio(nombreColonia, idMunicipio);
//    }
//
//    @GetMapping("direccion/{codigoPostal}")
//    @ResponseBody
//    public Result GetDireccionesByCodigoPostal(@PathVariable("codigoPostal") String CodigoPostal) {
//        return coloniaDAOImplementation.GetDireccionByCodigoPostal(CodigoPostal);
//    }
//
//    @GetMapping("direccionById/{idDireccion}")
//    @ResponseBody
//    public Result getDireccionById(@PathVariable("idDireccion") int idDireccion) {
//        return direccionJPADAOImplementation.GetAddressById(idDireccion);
//    }
}
