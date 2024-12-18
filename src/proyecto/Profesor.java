package proyecto;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Persistencia.ManejoPersistencia;

public class Profesor extends Usuario {
	


	//Atributos
    private List<LearningPath> learningPathsCreados;
    
    //Constructor
	public Profesor(String nombre, String correo, String contrasena) {
		super(nombre, correo, contrasena);
        this.learningPathsCreados = new ArrayList<>();
		// TODO Auto-generated constructor stub
	}
	
	//Get and set
	public List<LearningPath> getLearningPathsCreados() {
		return learningPathsCreados;
	}

	
	//Metodos
	
	@Override
	public String getTipoUsuario() {
		return "Profesor";
	}
	
	@Override
	public void verLearningPaths() {
        System.out.println("Learning Paths creados:");
        for (LearningPath lp : learningPathsCreados) {
            System.out.println("- " + lp.getTitulo());
        }
	}

	
	
	@Override
	public void darReseñaActividad(Actividad actividad, String texto, float rating) {
			Reseña reseña = new Reseña(texto, rating);
			reseña.hacerReseña(actividad);;
	}
	
	public Boolean verificarActividadExistente(String nombreLp, Actividad actividad) {
		Boolean rta = false;
		for (LearningPath lp: learningPathsCreados) {
			if (lp.getTitulo() == nombreLp) {
				List<Actividad> actividades = lp.getActividades();
				for (Actividad a: actividades) {
					if (a.equals(actividad)) {
						rta = true;
					}
				}
			}
		}
		return rta;
		
		
	}
	
	public Boolean verificarPathExistente(LearningPath lp) {
		Boolean rta = false;
		for (LearningPath lpp: learningPathsCreados) {
			if (lpp.equals(lp)) {
						rta = true;
					}
				}
		return rta;
	}
	
    public LearningPath crearLearningPath(String titulo, String descripcion, String objetivos, String nivelDificultad, int duracionEstimada, ManejoPersistencia sistema) {
    	
    	LearningPath nuevoLP = new LearningPath(titulo, descripcion, objetivos, nivelDificultad, this, duracionEstimada);
        learningPathsCreados.add(nuevoLP);
        System.out.println("Learning Path creado exitosamente.");
        sistema.crearPathData(nuevoLP);
        return nuevoLP;
    }
    
    public void editarAtributosPath(LearningPath lp) {
        boolean opcionValida = false;
        Scanner scanner = new Scanner(System.in);
        while (!opcionValida) {
            try {
                System.out.println("\nSeleccione el atributo que desea editar:");
                System.out.println("1. Título");
                System.out.println("2. Descripción");
                System.out.println("3. Objetivos");
                System.out.println("4. Nivel de Dificultad");
                System.out.print("Opción: ");
                int opcion = Integer.parseInt(scanner.nextLine());

                if (opcion < 1 || opcion > 5) {
                    System.out.println("Selección no válida. Por favor, intente nuevamente.");
                } else {
                    System.out.print("Ingrese el nuevo valor: ");
                    String nuevoValor = scanner.nextLine();
                    switch (opcion) {
                        case 1:
                            lp.setTitulo(nuevoValor);
                            opcionValida = true;
                        case 2:
                            lp.setDescripcion(nuevoValor);
                            opcionValida = true;
                        case 3:
                            lp.setObjetivos(nuevoValor);
                            opcionValida = true;
                        case 4:
                            lp.setNivelDificultad(nuevoValor);
                            opcionValida = true;
                    }
                    
                    System.out.println("Atributo actualizado exitosamente.");
                }
            } catch (Exception e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }
    }
    
    public Actividad crearActividad(Scanner scanner) {
    	
    	Actividad act = null;
    	if (learningPathsCreados.size()>0) {
            
        	boolean opcionValida = false;
        	while (!opcionValida) {
    	    	try {
    	    		System.out.println("¿Qué tipo de actividad quiere crear? Seleccione el número:");
    	            System.out.println("1. Recurso educativo\n2. Encuesta\n3. Tarea\n4. Quiz\n5. Examen");
    	        	int tipo = Integer.parseInt(scanner.nextLine());
    	        	
    	            if (tipo < 1 || tipo > 5) {
    	            	System.out.println("Selección no válida. Por favor, intente nuevamente.");
    	            } else if (tipo == 1) {
    	            	opcionValida = true;
    	            	act = crearRecurso(scanner);
    	            } else if (tipo == 2) {
    	            	opcionValida = true;
    	            	act = crearEncuesta(scanner);
    	            } else if (tipo == 3) {
    	            	opcionValida = true;
    	            	act = crearTarea(scanner);
    	            } else if (tipo == 4) {
    	            	opcionValida = true;
    	            	act = crearQuiz(scanner);
    	            } else {
    	            	opcionValida = true;
    	            	act = crearExamen(scanner);
    	            }
    	        } catch (Exception e) {
    	            System.out.println("Entrada no válida. Por favor, ingrese un número.");
    	        }
        	}
    	} else {
    		System.out.println("Debe crear un learningPath antes de crear una actividad.");
    	}
		return act;
    }
    
    private RecursoEducativo crearRecurso(Scanner scanner) {
    	System.out.println("¿Para cual learning path quiere crear el recurso educativo?\n");
        for (int i = 0; i < learningPathsCreados.size(); i++) {
            LearningPath lp = learningPathsCreados.get(i);
            System.out.println((i + 1) + ". " + lp.getTitulo() + " - " + lp.getDescripcion());
    	}

        LearningPath path = null;
        while (true) {
            try {
                System.out.print("Ingrese el número del Learning Path: ");
            	int seleccion = Integer.parseInt(scanner.nextLine());
                if (seleccion < 1 || seleccion > learningPathsCreados.size()) {
                	System.out.println("Selección no válida. Por favor, intente nuevamente.");
                    ;
                } else {
                	path = learningPathsCreados.get(seleccion - 1);
                	break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("Ingrese el nombre del recurso: ");
        String nom = scanner.nextLine();
        
        
        System.out.print("Ingrese la descripción del recurso: ");
        String des = scanner.nextLine();
        
        System.out.print("Ingrese el objetivo del recurso: ");
        String obj = scanner.nextLine();
        
        System.out.print("Ingrese el nivel de dificultad del recurso (Bajo, Medio, Alto): ");
        String dif = scanner.nextLine();
        
        int duracionEsperada = 0;
        while (true) {
            try {
                System.out.print("Ingrese la duración esperada (en minutos) para completar la encuesta: ");
                duracionEsperada = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
        
        System.out.print("¿Es obligatorio? (si/no) ");
        String obligatorio = scanner.nextLine().toLowerCase();
        
        boolean obl = false;
        if (obligatorio.equals("si")) {
        	obl = true;
        } 
        System.out.print("Ingrese el tipo de recurso: ");
        String tipo = scanner.nextLine();
        
        System.out.print("Ingrese el enlace del recurso: ");
        String enl = scanner.nextLine();
        
        RecursoEducativo nuevo = new RecursoEducativo(path,nom, des, obj, dif, duracionEsperada, obl, tipo, enl, this);
        
        añadirActividadALearningPath(nuevo);
    	
        return nuevo;
    }
    
    private Encuesta crearEncuesta(Scanner scanner) {
    	System.out.println("¿Para cual learning path quiere crear la encuesta?\n");
        for (int i = 0; i < learningPathsCreados.size(); i++) {
            LearningPath lp = learningPathsCreados.get(i);
            System.out.println((i + 1) + ". " + lp.getTitulo() + " - " + lp.getDescripcion());
    	}
        LearningPath path = null;
        
        while (true) {
            try {
                System.out.print("Ingrese el número del Learning Path: ");
            	int seleccion = Integer.parseInt(scanner.nextLine());
                if (seleccion < 1 || seleccion > learningPathsCreados.size()) {
                	System.out.println("Selección no válida. Por favor, intente nuevamente.");
                    ;
                } else {
                	path = learningPathsCreados.get(seleccion - 1);
                	break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("Ingrese un nombre para la encuesta: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese una descripción para la encuesta: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ingrese el objetivo de la encuesta: ");
        String objetivo = scanner.nextLine();

        System.out.print("Ingrese el nivel de dificultad de la encuesta: ");
        String nivelDificultad = scanner.nextLine();

        int duracionEsperada = 0;
        while (true) {
            try {
                System.out.print("Ingrese la duración esperada (en minutos) para completar la encuesta: ");
                duracionEsperada = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingrese un número válido.");
            }
        }

        System.out.print("¿Es la encuesta obligatoria? (si/no): ");
        boolean obligatorio = false;
        String esObligatorio = scanner.nextLine().toLowerCase();
        if (esObligatorio.equals("si")) {
            obligatorio = true;
        }
        
        Encuesta nuevo = new Encuesta(path, nombre, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, this);
        
        nuevo.agregarPregunta(scanner);
        
        añadirActividadALearningPath(nuevo);

        System.out.println("Encuesta creada exitosamente.\n");

    	return nuevo;
    }
    
    private Tarea crearTarea(Scanner scanner) {
    	System.out.println("¿Para cual learning path quiere crear la tarea?\n");
        for (int i = 0; i < learningPathsCreados.size(); i++) {
            LearningPath lp = learningPathsCreados.get(i);
            System.out.println((i + 1) + ". " + lp.getTitulo() + " - " + lp.getDescripcion());
    	}

        LearningPath path = null;
        while (true) {
            try {
                System.out.print("Ingrese el número del Learning Path: ");
            	int seleccion = Integer.parseInt(scanner.nextLine());
                if (seleccion < 1 || seleccion > learningPathsCreados.size()) {
                	System.out.println("Selección no válida. Por favor, intente nuevamente.");
                    ;
                } else {
                	path = learningPathsCreados.get(seleccion - 1);
                	break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("Ingrese un nombre para la tarea: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese una descripción para la tarea: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ingrese el objetivo de la tarea: ");
        String objetivo = scanner.nextLine();

        System.out.print("Ingrese el nivel de dificultad de la tarea: ");
        String nivelDificultad = scanner.nextLine();


        int duracionEsperada = 0;
        while (true) {
            try {
                System.out.print("Ingrese la duración esperada (en minutos) para completar la tarea: ");
                duracionEsperada = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("¿Es la tarea obligatoria? (si/no): ");
        boolean obligatorio = false;
        String esObligatorio = scanner.nextLine().toLowerCase();
        if (esObligatorio.equals("si")) {
            obligatorio = true;
        }
        
        Tarea tarea = new Tarea(path,nombre, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, this);

        añadirActividadALearningPath(tarea);

        System.out.println("Tarea creada exitosamente.\n");
        
    	return tarea;
    }
    
    private Quiz crearQuiz(Scanner scanner) {
    	
    	System.out.println("¿Para cual learning path quiere crear el quiz?\n");
        for (int i = 0; i < learningPathsCreados.size(); i++) {
            LearningPath lp = learningPathsCreados.get(i);
            System.out.println((i + 1) + ". " + lp.getTitulo() + " - " + lp.getDescripcion());
    	}

        LearningPath path = null;
        while (true) {
            try {
                System.out.print("Ingrese el número del Learning Path: ");
            	int seleccion = Integer.parseInt(scanner.nextLine());
                if (seleccion < 1 || seleccion > learningPathsCreados.size()) {
                	System.out.println("Selección no válida. Por favor, intente nuevamente.");
                    ;
                } else {
                	path = learningPathsCreados.get(seleccion - 1);
                	break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("Ingrese un nombre para el quiz: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese una descripción para el quiz: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ingrese el objetivo del quiz: ");
        String objetivo = scanner.nextLine();

        System.out.print("Ingrese el nivel de dificultad del quiz: ");
        String nivelDificultad = scanner.nextLine();

        int duracionEsperada = 0;
        while (true) {
            try {
                System.out.print("Ingrese la duración esperada (en minutos) para completar el quiz: ");
                duracionEsperada = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }


        double notaAprobacion = 0;
        while (true) {
            try {
                System.out.print("Ingrese la nota mínima para aprobar el quiz (0-100): ");
                notaAprobacion = Double.parseDouble(scanner.nextLine());
                if (notaAprobacion >= 0 && notaAprobacion <= 100) {
                    break;
                } else {
                    System.out.println("Error: La nota debe estar entre 0 y 100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        
        System.out.print("¿Es el quiz obligatorio? (si/no): ");
        boolean obligatorio = false;
        String esObligatorio = scanner.nextLine().toLowerCase();
        if (esObligatorio.equals("si")) {
            obligatorio = true;
        }

        Quiz quiz = new Quiz(path, nombre, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, notaAprobacion, this);

        boolean agregarMasPreguntas = true;
        while (agregarMasPreguntas) {
            quiz.agregarPregunta(scanner);
            System.out.print("¿Desea agregar otra pregunta? (si/no): ");
            String continuar = scanner.nextLine().toLowerCase();
            if (!continuar.equals("s")) {
                agregarMasPreguntas = false;
            }
        }

        añadirActividadALearningPath(quiz);
        System.out.println("Quiz creado exitosamente.\n");

        return quiz;
    }
    
    private Examen crearExamen(Scanner scanner) {
    	
    	System.out.println("¿Para cual learning path quiere crear el examen?\n");
        for (int i = 0; i < learningPathsCreados.size(); i++) {
            LearningPath lp = learningPathsCreados.get(i);
            System.out.println((i + 1) + ". " + lp.getTitulo() + " - " + lp.getDescripcion());
    	}

        LearningPath path = null;
        while (true) {
            try {
                System.out.print("Ingrese el número del Learning Path: ");
            	int seleccion = Integer.parseInt(scanner.nextLine());
                if (seleccion < 1 || seleccion > learningPathsCreados.size()) {
                	System.out.println("Selección no válida. Por favor, intente nuevamente.");
                    ;
                } else {
                	path = learningPathsCreados.get(seleccion - 1);
                	break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        System.out.print("Ingrese un nombre para el examen: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Ingrese una descripción para el examen: ");
        String descripcion = scanner.nextLine();

        System.out.print("Ingrese el objetivo del examen: ");
        String objetivo = scanner.nextLine();

        System.out.print("Ingrese el nivel de dificultad del examen: ");
        String nivelDificultad = scanner.nextLine();


        int duracionEsperada = 0;
        while (true) {
            try {
                System.out.print("Ingrese la duración esperada (en minutos) para completar el examen: ");
                duracionEsperada = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
        
        System.out.print("¿Es el examen obligatorio? (si/no): ");
        boolean obligatorio = false;
        String esObligatorio = scanner.nextLine().toLowerCase();
        if (esObligatorio.equals("si")) {
            obligatorio = true;
        }

        Examen examen = new Examen(path,nombre, descripcion, objetivo, nivelDificultad, duracionEsperada, obligatorio, this);
        
        boolean agregarMasPreguntas = true;
        while (agregarMasPreguntas) {
            examen.agregarPregunta(scanner);
            System.out.print("¿Desea agregar otra pregunta? (S/N): ");
            String continuar = scanner.nextLine().toLowerCase();
            if (!continuar.equals("s")) {
                agregarMasPreguntas = false;
            }
        }
        
        añadirActividadALearningPath(examen);

    	return null;
    }
    
    public void añadirActividadALearningPath(Actividad actividad) {
    	if (actividad.getCreador().equals(this)) {
        	LearningPath lp = actividad.getLearningPath();
        	lp.getActividades().add(actividad);
        	lp.añadirTiempoLp(actividad);
            System.out.println(actividad.getTipo() + "añadida exitosamente al Learning Path: " + lp.getDescripcion());
    	} else {
            System.out.println("No tiene los permisos para añadir la actividad.");
    	}
	}

        
    public void eliminarActividadDeLearningPath(Actividad actividad) {
    	LearningPath lp = actividad.getLearningPath();
        if (learningPathsCreados.contains(lp)) {
            if (lp.getActividades().remove(actividad)) {
                lp.reducirTiempoLp(actividad);
                System.out.println("Actividad eliminada exitosamente del Learning Path.");
            } else {
                System.out.println("La actividad no pertenece a este Learning Path.");
            }
        } else {
            System.out.println("Este Learning Path no fue creado por este profesor.");
        }
    }
	
	//El profesor edita el learning path editando sus actividades.
	public void editarActividad(Actividad actividad) {
		actividad.editar(this);
		actividad.getLearningPath().setFechaModificacion(new Date());
		actividad.getLearningPath().setVersion(actividad.getLearningPath().getVersion()+1);
	}
	
	public void agregarPrerrequisitoActividad(Actividad actividad, Actividad prerrequisito) {
		if (this.equals(actividad.getCreador())) {
			actividad.agregarPrerrequisito(prerrequisito);
		} else {
			System.out.println("No tiene los permisos para agregar el prerrequisito");
		}
	}
	
	public void agregarActividadSeguimiento(Actividad actividad, Actividad seguimiento) {
		if (this.equals(actividad.getCreador())) {
			actividad.agregarActividadSeguimiento(seguimiento);
		} else {
			System.out.println("No tiene los permisos para agregar la actividad de seguimiento");
		}
	}
	
	//Clonar actividad
	public Actividad clonarActividad(Actividad original) {
	    if (!original.getCreador().equals(this)) {
	        Actividad clon = original.clonarActividad(this);
	        if (clon != null) {
	        	System.out.println("Actividad clonada y guardada exitosamente.");
	        	return clon;
	        } else {
	            System.out.println("Error al clonar la actividad.");
	            return null;
	        }
	    } else {
	        System.out.println("No puedes clonar una actividad que tú mismo has creado.");
	        return null;
	    }
	}

	//Calificar
	public void calificarActividad(Actividad actividad, Scanner scanner) {
		if (actividad.getCreador().equals(this)){
			Set<Estudiante> estudiantes = actividad.getRespuesta().keySet();
			if (estudiantes.size()!=0) {
				for (Estudiante estudiante: estudiantes) {
					Map<Actividad, ProgresoActividad> mapa = estudiante.getProgresosAct();
					ProgresoActividad progreso = mapa.get(actividad);
					if (progreso.getResultado().equals("Enviada")) {
						String rta = actividad.getRespuesta().get(estudiante);
						if (actividad.getTipo().equals("Tarea")) {
							System.out.println("El estudiante "+ estudiante.nombre + " mando la tarea por el medio: \n" + rta);
						} else {
							System.out.println("La respuesta del estudiante "+ estudiante.nombre + " es: \n" + rta);
						}
			            System.out.print("¿Cómo desea calificarla?\n1.Aprobada\n2.Reprobada ");
			            String resultado = scanner.nextLine().toLowerCase();
			            if (resultado.equals("1")) {
							progreso.setResultado("Aprobada");
							System.out.println("Se ha calificado la actividad "+ actividad.descripcion+ " del estudiante " + estudiante.nombre + ". Su resultado es Aprobada.");
			            } else {
			            	progreso.setResultado("Reprobada");
							System.out.println("Se ha calificado la actividad "+ actividad.descripcion+ " del estudiante " + estudiante.nombre + ". Su resultado es Reprobada.");
			            }
						
					}
				}
			} else {
				System.out.println("No hay actividades por calificar.");
			}
		} else {
			System.out.println("No tiene los permisos para calificar esta actividad.");
		}
	}

	public void setLearningPathsCreados(List<LearningPath> listaLPCreados) {
		// TODO Auto-generated method stub
		this.learningPathsCreados = listaLPCreados;
		
	}
	

}
