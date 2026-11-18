const formateadorPesos = new Intl.NumberFormat('es-AR', {
  style: 'currency',
  currency: 'ARS',
  maximumFractionDigits: 0,
});

export function formatearPesos(monto) {
  return formateadorPesos.format(monto);
}

// La API devuelve el horario como "18:00:00"; mostramos solo "18:00".
export function formatearHorario(horario) {
  return String(horario).slice(0, 5);
}
