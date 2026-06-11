/** @type {import('tailwindcss').Config} */
module.exports = {
  // Scan the templates and the browser helpers (which build result markup) so
  // every utility used in markup or JS strings is generated.
  content: ['./src/main/resources/templates/**/*.html', './static/**/*.js'],
  theme: {
    extend: {
      colors: {
        bg: '#f6f8fc',
        'bg-soft': '#eef2f9',
        surface: '#ffffff',
        'surface-2': '#eef2fb',
        border: '#dde3ee',
        'border-soft': '#e9edf5',
        ink: '#0f1729',
        muted: '#5b6678',
        accent: '#365eed',
        'accent-hover': '#2a4ed1',
        success: '#16a34a',
        challenge: '#f59e0b',
        danger: '#dc2626',
      },
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        mono: ['ui-monospace', 'SFMono-Regular', 'Menlo', 'Consolas', 'monospace'],
      },
      borderRadius: {
        xl: '14px',
        lg: '9px',
      },
      boxShadow: {
        card: '0 1px 3px rgba(16, 24, 40, 0.06), 0 8px 24px rgba(16, 24, 40, 0.06)',
      },
    },
  },
  plugins: [],
};
