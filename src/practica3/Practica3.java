package practica3;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Practica3 {

	public final static int FILAS = 20;
	public final static int TAMANYO = 100;
	public static int pcontador;
	public static ArrayList<Proceso> tabla;

	public static void main(String[] args) {
		pcontador = 0;
		int opcion = 0;
		tabla = new ArrayList<Proceso>();

		JOptionPane
				.showMessageDialog(null,
						"Bienvenido a mi simulador de memoria\nBy: Lawrence Arthur Rider Garcia");

		for (int i = 0; i <= FILAS; i++) {
			tabla.add(inicializarProceso(i));
		}

		do {
			opcion = menu();
		} while (opcion != 4);
	}

	public static int menu() {
		int op = 0, op2 = 0;

		do {
			try {
				op = Integer
						.parseInt(JOptionPane
								.showInputDialog("Menu\n1. Añadir proceso\n2. Terminar proceso\n3. Mostrar tabla\n4. Salir\n"));
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null,
						"ERROR! No has introducido un tamaño");
				op = -1;
			}

			if (op < 1 || op > 4) {
				JOptionPane.showMessageDialog(null, "Error: opcion invalida");
			} else if (op == 1) {

				do {
					try {
						op2 = Integer
								.parseInt(JOptionPane
										.showInputDialog("Elige el algoritmo\n1. Siguiente hueco\n2. Peor hueco\n3. Salir"));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null,
								"ERROR! No has introducido un tamaño");
						op2 = -1;
					}

					if (op2 < 1 || op2 > 3) {
						JOptionPane.showMessageDialog(null,
								"Error: Opcion invalida");
					}
				} while (op2 < 1 || op2 > 3);
			}
		} while (op < 1 || op > 4);

		if (op == 1) {
			switch (op2) {
			case 1:
				mostarTabla();
				siguienteHueco();
				mostarTabla();
				break;
			case 2:
				mostarTabla();
				peorHueco();
				mostarTabla();
				break;
			case 3:
				break;
			}
		} else if (op == 2) {
			mostarTabla();
			eliminarProceso();
			mostarTabla();
			quitarEliminados();
		} else if (op == 3) {
			mostarTabla();
		}
		return (op);
	}

	public static void quitarEliminados() {
		for (int i = 0; i <= FILAS; i++) {
			if (tabla.get(i).nombre.contentEquals("eliminado")) {
				tabla.get(i).nombre = "vacio";
			}
		}
	}

	public static void mostarTabla() {
		String tablaString = "fila-num-tam-direcc-nombre\n";
		for (int i = 0; i <= FILAS; i++) {
			Integer numfila = tabla.get(i).num;
			String stringfila = "";
			if (numfila < 10) {
				stringfila = "0" + numfila.toString();
			} else {
				stringfila = numfila.toString();
			}
			Integer tamanyoproceso = tabla.get(i).tamanyo;
			String stringtamanyo = "";
			if (tamanyoproceso < 100) {
				stringtamanyo = "00" + tamanyoproceso.toString();
			} else {
				stringtamanyo = tamanyoproceso.toString();
			}
			Integer direcction = tabla.get(i).direccion;
			String stringdirec = "";
			if (direcction < 10) {
				stringdirec = "000" + direcction.toString();
			} else if (direcction < 1000) {
				stringdirec = "0" + direcction.toString();
			} else {
				stringdirec = direcction.toString();
			}

			tablaString += ("\u25EA " + stringfila + " - "
					+ tabla.get(i).proceso + " - " + stringtamanyo + " - "
					+ stringdirec + " - " + tabla.get(i).nombre + " \u25EA " + '\n');
		}
		tablaString += "___________________";
		JOptionPane.showMessageDialog(null, tablaString);
	}

	public static Proceso inicializarProceso(int i) {
		Proceso row = new Proceso();

		row.num = i;
		row.proceso = 0;
		row.tamanyo = 0;
		row.direccion = i * TAMANYO;
		row.nombre = "vacio";

		return (row);
	}

	public static int comprobarHueco(ArrayList<Huecos> fila) {
		int aux = 0;
		int pos = 0;

		for (int i = 0; i < fila.size(); i++) {
			if (aux < fila.get(i).hueco) {
				aux = fila.get(i).hueco;
			}
		}
		for (int j = 0; j < fila.size(); j++) {
			if (fila.get(j).hueco == aux) {
				pos = fila.get(j).posicion;
				break;
			}
		}
		return pos;
	}

	public static void eliminarProceso() {
		String nombre = JOptionPane
				.showInputDialog("Introduce el nombre del proceso a eliminar: ");
		for (int i = 0; i <= FILAS; i++) {
			if (tabla.get(i).nombre.contentEquals(nombre)) {
				tabla.get(i).proceso = 0;
				tabla.get(i).tamanyo = 0;
				tabla.get(i).nombre = "eliminado";
			}
		}
		pcontador--;
	}

	public static void anyadirProceso(int i, int kb, String nombre) {
		Boolean fuera = false;
		for (int j = i; j < (kb / TAMANYO) + i; j++) {
			if (j > FILAS) {
				fuera = true;
				break;
			} else {
				tabla.get(j).proceso = pcontador;
				tabla.get(j).tamanyo = kb;
				tabla.get(j).nombre = nombre;
			}
		}
		if (fuera == true) {
			for (int k = 0; k <= FILAS; k++) {
				if (tabla.get(k).nombre.contentEquals(nombre)) {
					tabla.get(k).proceso = 0;
					tabla.get(k).tamanyo = 0;
					tabla.get(k).nombre = " ";
				}
			}
			pcontador--;
			JOptionPane.showMessageDialog(null,
					"El proceso no puede ser añadido por falta memoria");
		}

	}

	public static void peorHueco() {

		ArrayList<Huecos> vector_huecos = new ArrayList<Huecos>();
		Huecos huecos;
		int pos, kb = 0, huecosPorDebajo = 0;

		String nombre = JOptionPane
				.showInputDialog("Introduce el nombre del proceso: ");

		try {
			kb = Integer.parseInt(JOptionPane
					.showInputDialog("Introduce el tamaño del proceso(kb): "));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"ERROR! No has introducido un tamaño");
			kb = -1;
		}

		for (int i = 0; i <= FILAS; i++) {
			if (tabla.get(i).proceso == 0) {
				int k = i;

				while (tabla.get(k).proceso == 0 && k < FILAS) {
					huecosPorDebajo++;
					k++;
				}
				huecos = new Huecos();
				huecos.posicion = i;
				huecos.hueco = huecosPorDebajo;
				vector_huecos.add(huecos);
				huecosPorDebajo = 0;
			}
		}

		if (kb > 0) {
			pos = comprobarHueco(vector_huecos);
			pcontador++;
			anyadirProceso(pos, kb, nombre);
		}
	}

	public static void siguienteHueco() {
		int kb = 0, kb2 = 0;
		Boolean anyadido = false;

		String nombre = JOptionPane
				.showInputDialog("Introduce el nombre del proceso: ");

		try {
			kb = Integer.parseInt(JOptionPane
					.showInputDialog("Introduce el tamaño del proceso(kb): "));
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null,
					"ERROR! No has introducido un tamaño");
			kb = -1;
		}
		for (int i = 0; i < FILAS; i++) {
			if (tabla.get(i).proceso == 0) {
				int k = i;

				while (tabla.get(k).proceso == 0 && anyadido == false
						&& k < FILAS) {
					kb2 += TAMANYO;

					if (kb > 0 && kb2 >= kb) {
						anyadido = true;
						pcontador++;
						anyadirProceso(i, kb, nombre);
					}
					k++;
				}
				kb2 = 0;
			}
		}

		if (kb > 0) {
			if (anyadido == true) {
				JOptionPane.showMessageDialog(null, "Proceso '" + nombre
						+ "' añadido.");
			} else {
				JOptionPane.showMessageDialog(null, "Error: el proceso '"
						+ nombre
						+ "' no puede ser añadido por falta de memoria.");
			}
		}
	}
}
