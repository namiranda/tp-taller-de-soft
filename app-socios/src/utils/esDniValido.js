// RN7: el DNI debe tener 7 u 8 dígitos numéricos, sin puntos ni espacios.
export function esDniValido(dni) {
  return typeof dni === 'string' && /^\d{7,8}$/.test(dni);
}
