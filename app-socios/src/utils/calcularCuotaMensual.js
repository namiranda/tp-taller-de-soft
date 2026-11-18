// RN6: la cuota mensual estimada es la suma de las cuotas de las actividades
// activas, con 10 % de descuento si son 2 actividades y 15 % si son 3.
export function calcularCuotaMensual(actividades) {
  if (!Array.isArray(actividades) || actividades.length === 0) {
    return 0;
  }

  const subtotal = actividades.reduce(
    (suma, actividad) => suma + Number(actividad.cuotaMensual),
    0
  );

  let descuento = 0;
  if (actividades.length === 2) {
    descuento = 0.1;
  } else if (actividades.length >= 3) {
    descuento = 0.15;
  }

  return subtotal * (1 - descuento);
}
