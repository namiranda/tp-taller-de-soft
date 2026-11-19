import { esDniValido } from './esDniValido';

describe('esDniValido (RN7)', () => {
  test.each(['1234567', '12345678'])('acepta %s', (dni) => {
    expect(esDniValido(dni)).toBe(true);
  });

  test.each(['12.345.678', '1234567a', '123456', '123456789', ' 12345678', ''])(
    'rechaza %s', (dni) => {
      expect(esDniValido(dni)).toBe(false);
    }
  );
});
