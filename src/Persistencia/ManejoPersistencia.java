package Persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;




import proyecto.Actividad;
import proyecto.LearningPath;
import proyecto.Profesor;
import proyecto.ProgresoActividad;
import proyecto.ProgresoPath;
import proyecto.Estudiante;


import proyecto.Encuesta;
import proyecto.Quiz;
import proyecto.RecursoEducativo;
import proyecto.Reseña;
import proyecto.Tarea;
import proyecto.Usuario;

public class ManejoPersistencia {
	private Map<String, LearningPath> mapaPaths = new HashMap<>();
	private Map<String, Actividad> mapaActividades = new HashMap<>();
	private Map<String, Usuario> mapaUsuarios = new HashMap<>();
	
	//SECCI0N PARA ACTIVIDADES
	
	
	//Resolver parametro creador desde controlador
	//Método para crear Actividad
	   public Map<String, Actividad> crearActividadData(Actividad actividad) {
		   mapaActividades.put(actividad.getNombre(), actividad);
		   guardarActividades();
		   return mapaActividades;
	   }
	   
	   //Buscar una actividad
	   public Actividad buscarActividad(String nombre) {
		   return mapaActividades.get(nombre);
	   }
	   
	//Modificar actividad
	   
	   public Map<String, Actividad> modificarActividad(int parametro, String modificar, String nombreActividad){
		   Actividad act = mapaActividades.get(nombreActividad);  
		   
		   if (parametro == 1) {
			   act.setNombre(modificar);
		   }
		   
		   else if(parametro == 2) {
			   act.setDescripcion(modificar);
		   }
		   
		   else if(parametro == 3) {
			   act.setObjetivo(modificar);
		   }
		   
		   else if(parametro == 4) {
			   act.setNivelDificultad(modificar);
		   }
		   
		   else if(parametro == 5) {
			   int m = Integer.parseInt(modificar);
			   act.setDuracionEsperada(m);
		   }
		   
		   else if(parametro == 6) {
			   if (act.getPrerrequisitosNombres().equals(null)) {
				   ArrayList<Actividad> prerequisitos = (ArrayList<Actividad>) act.getPrerrequisitos();
				   ArrayList<String> prerequisitosString = new ArrayList<>();
				   
				   for (Actividad prerequisito: prerequisitos) {
					   String pre = prerequisito.getNombre();
					   prerequisitosString.add(pre);
				   }
				   
				   
				   
				   String[] p = modificar.split(",");
				   
				   System.out.println("Ingrese 1 para agregar los prerrequisitos ingresados, o 2 para eliminarlos.");
				   
				   Scanner scanner = new Scanner(System.in);
			       int numero = scanner.nextInt();
			       scanner.close();
				   
				   if (numero == 1) {
					   for (String pre: p) {
						   prerequisitosString.add(pre);
					   }
				   }
				   
				   else if (numero == 2) {
					   for (String pre: p){
						   prerequisitosString.remove(pre);
					   }
				   }
				   
				   act.setPrerrequisitosNombre(prerequisitosString);
				   
			   }
			   else {
				   ArrayList<String> prerrequisitos =(ArrayList<String>) act.getPrerrequisitosNombres();
				   String[] p = modificar.split(",");
				   
				   System.out.println("Ingrese 1 para agregar los prerrequisitos ingresados, o 2 para eliminarlos.");
				   
				   Scanner scanner = new Scanner(System.in);
			       int numero = scanner.nextInt();
			       scanner.close();
				   
				   if (numero == 1) {
					   for (String pre: p) {
						   prerrequisitos.add(pre);
					   }
				   }
				   
				   else if (numero == 2) {
					   for (String pre: p){
						   prerrequisitos.remove(pre);
					   }
				   }
				   
			   }
		   }
		   
		   else if(parametro == 7) {
			   
			   long milisegundos = 1636627200000L; //para inicializar date
		       Date fecha = new Date(milisegundos);
		       
			   SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
	           
	           try {
	        	   fecha = formato.parse(modificar);
	           } catch (ParseException e) {
				e.printStackTrace();
	           		}
	           act.establecerFechaLimite(fecha);
		   }
		   
		   else if(parametro == 8) {
			   Boolean bol = Boolean.parseBoolean(modificar);
			   act.setObligatorio(bol);
		   }

		   
		   guardarActividades();
		   
		   return mapaActividades;
	   }
	   
	   
	//Guardar actividades
	   public Map<String, Actividad> guardarActividades(){
		   String nombreCSV = "data/datosActividades.csv";
		   
		   try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreCSV))) {
	           for (String llave: mapaActividades.keySet()) {
	        	   ArrayList<String> lineaActividad = formatoActividad(mapaActividades.get(llave));
	        	   String line = String.join(";", lineaActividad);
	               writer.write(line);
	               writer.newLine();
	           }
	      
	           System.out.println("Se ha guardado exitosamente.");
	           
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
		   
		   return mapaActividades;
	   }
	
	   public ArrayList<String> formatoActividad(Actividad a) {
		    ArrayList<String> rta = new ArrayList<>();

		    // Información general de la actividad
		    String nombre = a.getNombre();
		    String descripcion = a.getDescripcion();
		    String objetivo = a.getObjetivo();
		    String nivel = a.getNivelDificultad();
		    String duracion = String.valueOf(a.getDuracionEsperada());

		    // Formatear fecha
		    SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		    String fecha = formato.format(a.getFechaLimite());

		    String obligatorio = String.valueOf(a.isObligatorio());
		    String creador = a.getCreador().getNombre();

		    // Prerrequisitos
		    String prerequisitos = String.join(", ", a.getPrerrequisitosNombres());

		    // Añadir información general al formato
		    rta.add("Nombre: " + nombre);
		    rta.add("Descripción: " + descripcion);
		    rta.add("Objetivo: " + objetivo);
		    rta.add("Nivel: " + nivel);
		    rta.add("Duración: " + duracion);
		    rta.add("Fecha límite: " + fecha);
		    rta.add("Obligatorio: " + obligatorio);
		    rta.add("Creador: " + creador);
		    rta.add("Prerrequisitos: " + prerequisitos);

		    // Respuestas de estudiantes
		    rta.add("Respuestas de estudiantes:");
		    Map<Estudiante, String> respuestas = a.getRespuesta();
		    for (Map.Entry<Estudiante, String> entry : respuestas.entrySet()) {
		        Estudiante estudiante = entry.getKey();
		        String respuesta = entry.getValue();
		        rta.add("Estudiante: " + estudiante.getNombre() + ", Respuesta: " + respuesta);
		    }

		    // Reseñas
		    rta.add("Reseñas:");
		    List<Reseña> reseñas = a.getReseñas();
		    for (Reseña reseña : reseñas) {
		        String rating = String.valueOf(reseña.getRating());
		        String comentario = reseña.getTexto() != null && !reseña.getTexto().isEmpty() ? reseña.getTexto() : "\"\"";
		        rta.add("Rating: " + rating + ", Comentario: " + comentario);
		    }

		    return rta;
		}
	
	//Se lee el archivo y se crea el mapa de actividades

	    public Map<String, Actividad> crearMapaActividades(Profesor profesor) {
	        String nombreCSV = "data/datosActividades.csv";
	        Scanner scanner = new Scanner(System.in);

	        try (BufferedReader br = new BufferedReader(new FileReader(nombreCSV))) {
	            String line;

	            if ((line = br.readLine()) == null) {
	                System.out.println("No existen actividades. Debe primero crear una.");

	                boolean agregarMasActividades = true;
	                while (agregarMasActividades) {
	                    // Usar el método crearActividad del profesor
	                    Actividad nuevaActividad = profesor.crearActividad(scanner);
	                    if (nuevaActividad != null) {
	                        crearActividadData(nuevaActividad);
	                        System.out.println("Actividad añadida al mapa.");
	                    }

	                    System.out.print("¿Desea agregar otra actividad? (si/no): ");
	                    String continuar = scanner.nextLine().toLowerCase();
	                    if (!continuar.equals("si")) {
	                        agregarMasActividades = false;
	                    }
	                }
	            } else {
	                // Leer actividades del archivo CSV
	                while ((line = br.readLine()) != null) {
	                    String[] values = line.split(";");
	                    Scanner csvScanner = new Scanner(String.join("\n", values));

	                    // Delegar la creación de la actividad al profesor
	                    Actividad actividad = profesor.crearActividad(csvScanner);
	                    if (actividad != null) {
	                        crearActividadData(actividad);
	                    }
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Las actividades se han cargado exitosamente.");
	        return mapaActividades;
	    }
		
	//FIN SECCION PARA ACTIVIDADES
		
		
	//SECCION PARA LEARNING PATHS
	    /**
	     * Crear o actualizar un LearningPath en el mapa y persistir los cambios.
	     */
	    public Map<String, LearningPath> crearPathData(LearningPath lp) {
	        mapaPaths.put(lp.getTitulo(), lp);
	        guardarPaths();
	        return mapaPaths;
	    }

	    /**
	     * Buscar un LearningPath por nombre.
	     */
	    public LearningPath buscarPath(String nombre) {
	        return mapaPaths.get(nombre);
	    }

	    /**
	     * Modificar un LearningPath según un parámetro específico.
	     */
	    public Map<String, LearningPath> modificarPath(int parametro, String modificar, String path) {
	        LearningPath lp = mapaPaths.get(path);
	        if (lp == null) {
	            System.out.println("Error: LearningPath no encontrado.");
	            return mapaPaths;
	        }

	        Date fechaHoy = new Date();
	        lp.setFechaModificacion(fechaHoy);

	        switch (parametro) {
	            case 1 -> lp.setTitulo(modificar);
	            case 2 -> lp.setDescripcion(modificar);
	            case 3 -> lp.setObjetivos(modificar);
	            case 4 -> {
	                String nivel = switch (modificar.toLowerCase()) {
	                    case "bajo" -> "Bajo";
	                    case "medio" -> "Medio";
	                    case "alto" -> "Alto";
	                    default -> throw new IllegalArgumentException("Nivel de dificultad no válido.");
	                };
	                lp.setNivelDificultad(nivel);
	            }
	            case 5 -> {
	                
	                lp.añadirTiempoLp(mapaActividades.get(modificar)); // Debes pasar una instancia de Actividad que modifique el tiempo.
	            }
	            case 6 -> {
	                List<Actividad> actividades = lp.getActividades();
	                Actividad actividad = mapaActividades.get(modificar);
	                if (actividad == null) {
	                    System.out.println("Error: Actividad no encontrada.");
	                    return mapaPaths;
	                }

	                System.out.println("Ingrese 1 para agregar la actividad o 2 para eliminarla:");
	                Scanner scanner = new Scanner(System.in);
	                int opcion = scanner.nextInt();
	                if (opcion == 1) actividades.add(actividad);
	                else if (opcion == 2) actividades.remove(actividad);
	                else System.out.println("Opción no válida.");
	                lp.setActividades(actividades);
	            }
	            default -> System.out.println("Parámetro no válido.");
	        }

	        guardarPaths();
	        return mapaPaths;
	    }

	    /**
	     * Formatear un LearningPath en una lista de cadenas para guardar en un archivo.
	     */
	    public ArrayList<String> formatoPath(LearningPath lp) {
	        ArrayList<String> rta = new ArrayList<>();
	        rta.add(lp.getTitulo());
	        rta.add(lp.getDescripcion());
	        rta.add(lp.getObjetivos());
	        rta.add(lp.getNivelDificultad());
	        rta.add(String.valueOf(lp.getDuracionEstimada()));
	        rta.add(new SimpleDateFormat("dd-MM-yyyy").format(lp.getFechaCreacion()));
	        rta.add(new SimpleDateFormat("dd-MM-yyyy").format(lp.getFechaModificacion()));
	        rta.add(lp.getCreador().getNombre());

	        // Formatear actividades asociadas
	        String actividades = String.join(",", lp.getActividades().stream().map(Actividad::getNombre).toList());
	        rta.add(actividades);

	        return rta;
	    }

	    /**
	     * Guardar el mapa de LearningPaths en un archivo CSV.
	     */
	    public Map<String, LearningPath> guardarPaths() {
	        String nombreCSV = "data/datosPaths.csv";

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreCSV))) {
	            for (String llave : mapaPaths.keySet()) {
	                ArrayList<String> lineaPath = formatoPath(mapaPaths.get(llave));
	                String line = String.join(";", lineaPath);
	                writer.write(line);
	                writer.newLine();
	            }
	            System.out.println("LearningPaths guardados exitosamente.");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return mapaPaths;
	    }
		
	    /**
	     * Leer un archivo CSV y crear el mapa de LearningPaths.
	     */
	    public Map<String, LearningPath> crearMapaPaths() {
	        String nombreCSV = "data/datosPaths.csv";

	        try (BufferedReader br = new BufferedReader(new FileReader(nombreCSV))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(";");
	                String titulo = values[0];
	                String descripcion = values[1];
	                String objetivos = values[2];
	                String nivelDificultad = values[3];
	                int duracionEstimada = Integer.parseInt(values[4]);
	                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
	                Date fechaCreacion = formatoFecha.parse(values[5]);
	                Date fechaModificacion = formatoFecha.parse(values[6]);

	                // Profesor creador
	                String nombreCreador = values[7];
	                Profesor creador = new Profesor(nombreCreador, "correo@example.com", "password");

	                // Crear el LearningPath
	                LearningPath lp = new LearningPath(titulo, descripcion, objetivos, nivelDificultad, creador, duracionEstimada);
	                lp.setFechaCreacion(fechaCreacion);
	                lp.setFechaModificacion(fechaModificacion);

	                // Procesar actividades
	                List<Actividad> actividades = new ArrayList<>();
	                String[] nombresActividades = values[8].split(",");
	                for (String nombreActividad : nombresActividades) {
	                    Actividad actividad = cargarActividad(nombreActividad);
	                    if (actividad != null) actividades.add(actividad);
	                }
	                lp.setActividades(actividades);

	                // Procesar reseñas
	               

	                mapaPaths.put(lp.getTitulo(), lp);
	            }
	        } catch (IOException | ParseException e) {
	            e.printStackTrace();
	        }

	        System.out.println("LearningPaths cargados exitosamente.");
	        return mapaPaths;
	    }

	    /**
	     * Cargar una actividad desde el mapa de actividades.
	     */
	    private Actividad cargarActividad(String nombreActividad) {
	        return mapaActividades.getOrDefault(nombreActividad, null);
	    }
	
	
	//FIN SECCION PARA LEARNING PATHS
		
	//SECCION PARA USUARIOS
			public Map<String, Usuario> crearUsuarioData(String nombre, String email, String clave, int tipo) {
				
				if (tipo == 1) {
					Profesor usu = new Profesor(nombre, email, clave);
					mapaUsuarios.put(usu.getNombre(), usu);
				}
				else if (tipo == 2) {
					Estudiante usu = new Estudiante(nombre, email, clave);
					mapaUsuarios.put(usu.getNombre(), usu);
				}
				
				guardarUsuarios(tipo);
				   
				return mapaUsuarios;
			   }
			
			//Buscar un usuario
			   public Usuario buscarUsuario(String nombre, int tipo) {
				   Usuario usu = mapaUsuarios.get(nombre);
				   
				   if (tipo == 1) {
					   usu = (Profesor) usu;
					  
				   }
				   else if (tipo == 2) {
					   usu = (Estudiante) usu;
					   
				   }
				   return usu;
				   
			   }

			//Modificar usuario
			   
			   public Map<String, Usuario> modificarUsuario(int parametro, String modificar, String usuario, int tipo){
				   if (tipo == 1) {
					   Profesor profe = (Profesor) mapaUsuarios.get(usuario);
					   if (parametro == 1) {
						   profe.setNombre(modificar);
					   }
					   else if (parametro == 2) {
						   profe.setEmail(modificar);
					   }
					   else if (parametro ==3) {
						   profe.setClave(modificar);
					   }
				   
				   }
				   else if (tipo == 2) {
					   Estudiante estu = (Estudiante) mapaUsuarios.get(usuario);
					   if (parametro == 1) {
						   estu.setNombre(modificar);
					   }
					   else if (parametro == 2) {
						   estu.setEmail(modificar);
					   }
					   else if (parametro ==3) {
						   estu.setClave(modificar);
					   }
				  
				   }
				 
				   guardarUsuarios(tipo);
				   
					return mapaUsuarios;
			   }
			
			 //formato en string de cada usuario
				public ArrayList<String> formatoUsuario(Usuario usu, int tipo){
					   ArrayList<String> rta = new ArrayList<>(); 
					   
					   if (tipo == 1) {
						   
						   String nombre = usu.getNombre();
						   String email = usu.getCorreo();
						   String clave = usu.getContrasena();
						   
						   rta.add(String.valueOf(tipo));
						   rta.add(nombre);
						   rta.add(email);
						   rta.add(clave);
					   }
					   else if(tipo == 2) {
						   Estudiante estu = (Estudiante) usu;
						   
						   String nombre = estu.getNombre();
						   String email = estu.getCorreo();
						   String clave = estu.getContrasena();
						   
						   String controlPath = "";
						   
						   HashMap<LearningPath, ProgresoPath> control = (HashMap<LearningPath, ProgresoPath>) estu.getProgresoPaths();
						   
						   for (ProgresoPath c: control) {
							   String miniString = "";
							   
							   String nombrePath = c.getLp().getTitulo();
							   String enCurso = String.valueOf(c.isCompletado());
							   
							   SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
						       String fechaInicio = formato.format(c.getFechaInicioPath());
						       String fechaFinalizacion = formato.format(c.getFechaFinPath());
						       
						       String totalActividades = String.valueOf((c.getActividadesRealizadas().size()));
						       String actividadesCompletadas = String.valueOf(c.getActividadesRealizadas());
						       String progreso = String.valueOf(c.getPorcentajePath());
						       String tasaexito =  String.valueOf(c.getTasaExito());
						       String tasafracaso = String.valueOf(c.getTasaFracaso());
						       String controlActividades = "";
						       
						       HashMap<Actividad, ProgresoActividad> progresoAct = (HashMap<Actividad, ProgresoActividad>) estu.getProgresosAct();
						       
						       for (ProgresoActividad ca: progresoAct) {
						    	   String miniCa = "";
						    	   
						    	   String nombreCa = ca.getNombreActividad();
						    	   String estadoCa = ca.getEstado();
						    	   String fechaCompletarCa = formato.format(ca.getFechaCompletar());
						    	   String tiempoDedicadoCa = String.valueOf(ca.getTiempoDedicado());
						    	   String tasaExito = String.valueOf(ca.getTasaExitoFracaso());
						    	   String calificacionCa = String.valueOf(ca.getCalificacion());
						    	   String medioEntrega = ca.getMedioEntrega();
						    	   
						    	   miniCa = miniCa + nombreCa + ", " + estadoCa + ", " + fechaCompletarCa + ", ";
						    	   miniCa = miniCa + tiempoDedicadoCa + ", " + tasaExito + ", " + calificacionCa + ", " + medioEntrega + "/";
						    	   
						    	   controlActividades = controlActividades + miniCa;
						    	   
						    	   //revisar como se estan separando los datos
						       }
						       
						       miniString = miniString + nombrePath + "$" + enCurso + "$" + fechaInicio + "$" + fechaFinalizacion;
						       miniString = miniString + totalActividades + "$" + actividadesCompletadas + "$" + progreso + "$" + controlActividades + "*";
						       
						       controlPath = controlPath + miniString;
							   
						   }
						   
						   rta.add(String.valueOf(tipo));
						   rta.add(nombre);
						   rta.add(email);
						   rta.add(clave);
						   rta.add(controlPath);
					   }
					   
					   return rta;
			   
				}
				
				
				//Guardar usuarios
				   public Map<String, Usuario> guardarUsuarios(int tipo){
					   String nombreCSV = "data/datosUsuarios.csv";
					   
					   try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreCSV))) {
				           for (String llave: mapaUsuarios.keySet()) {
				        	   ArrayList<String> lineaPath = formatoUsuario(mapaUsuarios.get(llave), tipo);
				        	   String line = String.join(";", lineaPath);
				               writer.write(line);
				               writer.newLine();
				           }
				      
				           System.out.println("Se ha guardado exitosamente.");
				           
				       } catch (IOException e) {
				           e.printStackTrace();
				       }
					   
					   return mapaUsuarios;
				   }
			
				   
				 //Se lee el archivo y se crea el mapa de usuarios
					public Map<String, Usuario> crearMapaUsuarios(){
						String nombreCSV = "data/datosUsuarios.csv";
						
						long milisegundos = 1636627200000L; //para inicializar date
					       Date fechaInicio = new Date(milisegundos);
					       Date fechaFinalizacion = new Date(milisegundos);
					       Date fechaCompletar = new Date(milisegundos);
						
						try (BufferedReader br = new BufferedReader(new FileReader(nombreCSV))) {
				            String line;
				            while ((line = br.readLine()) != null) {
				                String[] values = line.split(";");
				                
				                int tipo = Integer.parseInt(values[0]);
				                if (tipo == 1) {
				                	String nombre = values[1];
				                	String email = values[2];
				                	String clave = values[3];
				                	
				                	Profesor profesor = new Profesor(nombre, email, clave);
				                	mapaUsuarios.put(profesor.getNombre(), profesor);
				                }
				                else if (tipo == 2) {
				                	String nombre = values[1];
				                	String email = values[2];
				                	String clave = values[3];
				                	
				                	ArrayList<ControlPath> controlPaths = new ArrayList<>();
				                	
				                	String[] lineaControlPaths = values[4].split("*");
				                	for (String cp: lineaControlPaths) {
				                		String[] atributosControlPath = cp.split("$");
				                		String nombrePath = atributosControlPath[0];
				                		Boolean enCurso = Boolean.parseBoolean(atributosControlPath[1]);
				                		
				                		SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
						                String f = atributosControlPath[2];
				                		
						                try {
							     			fechaInicio = formato.parse(f);
							     		} catch (ParseException e) {
							     			e.printStackTrace();
							     		}
						                
						                String fa = atributosControlPath[3];
				                		
						                try {
							     			fechaFinalizacion = formato.parse(fa);
							     		} catch (ParseException e) {
							     			e.printStackTrace();
							     		}
						                
						                int totalActividades = Integer.parseInt(atributosControlPath[4]);
						                int actividadesCompletadas = Integer.parseInt(atributosControlPath[5]);
						                float progreso = Float.parseFloat(atributosControlPath[6]);
						                
						                ArrayList<ControlActividad> actividades = new ArrayList<>();
						                
						                String[] controlesActividad = atributosControlPath[7].split("/");
						                
						                for (String lineaControlActividad: controlesActividad) {
						                	String[] atributosActividad = lineaControlActividad.split(",");
						                	
						                	String nombreAc = atributosActividad[0];
						                	String estadoAc = atributosActividad[1];
						                	
						                	String fc = atributosActividad[2];
					                		
							                try {
								     			fechaCompletar = formato.parse(fc);
								     		} catch (ParseException e) {
								     			e.printStackTrace();
								     		}
						                	
						                	float tiempoDedicadoAc = Float.parseFloat(atributosActividad[3]);
						                	float tasaExitoFAc = Float.parseFloat(atributosActividad[4]);
						                	float calificacionAc = Float.parseFloat(atributosActividad[5]);
						                	
						                	String medioEntregaAc = atributosActividad[6];
						                	
						                	ControlActividad controlActividadLocal = new ControlActividad(nombreAc, medioEntregaAc);
						                	controlActividadLocal.setEstado(estadoAc);
						                	controlActividadLocal.setFechaCompletar(fechaCompletar);
						                	controlActividadLocal.setTiempoDedicado(tiempoDedicadoAc);
						                	controlActividadLocal.setTasaExitoFracaso(tasaExitoFAc);
						                	controlActividadLocal.setCalificacion(calificacionAc);
						                	controlActividadLocal.setMedioEntrega(medioEntregaAc);
						                	
						                	actividades.add(controlActividadLocal);
						                	
						                }
						                
						                ControlPath controlPathLocal = new ControlPath(nombrePath, fechaInicio, fechaFinalizacion,
						                		totalActividades, progreso);
						                controlPathLocal.setEnCurso(enCurso);
						                controlPathLocal.setActividadesCompletadas(actividadesCompletadas);
						                controlPathLocal.setActividades(actividades);
						                
						                controlPaths.add(controlPathLocal);
						                
				                	}
				                	
				                	Estudiante estudiante = new Estudiante(nombre, email, clave);
				                	estudiante.setControlPaths(controlPaths);
				                	
				                	mapaUsuarios.put(estudiante.getNombre(), estudiante);
				                	
				                	
				                }
				              
				                
				            }
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
						
						System.out.println("Los usuarios se han cargado exitosamente.");
						
						return mapaUsuarios;
					}	   
				   

	//FIN SECCION PARA USUARIOS
	}
	