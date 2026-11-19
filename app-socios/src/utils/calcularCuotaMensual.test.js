import { calcularCuotaMensual } from './calcularCuotaMensual';

describe('calcularCuotaMensual (RN6)', () => {
  test('sin actividades la cuota es cero', () => {
    expect(calcularCuotaMensual([])).toBe(0);
  });

  test('una actividad no tiene descuento', () => {
    expect(calcularCuotaMensual([{ cuotaMensual: 10000 }])).toBe(10000);
  });

  test('dos actividades tienen un 10 % de descuento', () => {
    expect(calcularCuotaMensual([{ cuotaMensual: 10000 }, { cuotaMensual: 20000 }])).toBe(27000);
  });

  test('tres actividades tienen un 15 % de descuento', () => {
    expect(calcularCuotaMensual([{ cuotaMensual: 10000 }, { cuotaMensual: 20000 }, { cuotaMensual: 30000 }])).toBe(51000);
  });
});
