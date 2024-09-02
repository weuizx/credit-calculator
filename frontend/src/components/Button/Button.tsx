import { useState } from 'react';
import styles from './Button.module.scss';
import Loader from '../Loader/Loader';

export type ButtonProps = {
  state?: 'enabled' | 'loading' | 'disabled';
  style?: React.CSSProperties;
  onClick?: () => void;
};

const Button = (props: React.PropsWithChildren<ButtonProps>) => {
  const { state = 'enabled', style = {}, onClick = () => {}, children } = props;
  const [clickOverlayStyle, setClickOverlayStyle] = useState({
    left: 0,
    top: 0,
    width: 0,
    height: 0,
  });
  const [isPressed, setIsPressed] = useState(false);

  const handleMouseDown = (e: React.MouseEvent) => {
    const rect = e.currentTarget.getBoundingClientRect();
    const width = Math.sqrt(Math.pow(rect.width, 2) + Math.pow(56, 2)) / 10; //вычисляем диагональ кнопки и уменьшаем её в 10 раз, это будет радиус круга, который появляется при клике на кнопку, а затем увеличивается в 20 раз, покрывая кнопку целиком
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;
    setClickOverlayStyle({ left: x, top: y, width, height: width });
    setIsPressed(true);
  };

  const handleMouseUp = () => {
    setIsPressed(false);
    onClick();
  };

  return (
    <button
      onMouseDown={handleMouseDown}
      onMouseUp={handleMouseUp}
      className={`button ${styles.button} ${styles[state]}`}
      style={style}
    >
      <div className={styles.content}>{children}</div>
      {isPressed && <span className={styles['click-overlay']} style={clickOverlayStyle}></span>}
      <div
        className={`${styles.overlay} ${state === 'loading' ? styles['shimmer_overlay'] : ''}`}
      ></div>
      <Loader style={{ width: 24, height: 24, opacity: state === 'loading' ? 1 : 0 }} />
    </button>
  );
};

export default Button;
