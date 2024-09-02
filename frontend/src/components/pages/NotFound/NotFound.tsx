import styles from "./NotFound.module.scss";

const NotFound: React.FC = () => {
  return (
    <div className={`${styles.container} container`}>
      <h1>404</h1>
      <p>Страница не найдена</p>
    </div>
  );
};

export default NotFound;
