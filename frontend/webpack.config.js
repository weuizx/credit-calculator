const path = require('path');
const { createProxyMiddleware } = require('http-proxy-middleware');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

module.exports = (env) => {
  return {
    entry: './src/index.tsx',
    output: {
      path: path.resolve(__dirname, 'dist'),
      filename: '[name].[contenthash].js',
      chunkFilename: '[name].[contenthash].js',
      publicPath: '/',
    },
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: 'ts-loader',
          exclude: /node_modules/,
        },
        {
          test: /\.[tj]s$/,
          use: 'ts-loader',
          exclude: /node_modules/,
        },
        {
          test: /\.(?:ico|gif|png|jpg|jpeg|svg|webp)$/i,
          type: 'asset/resource',
        },
        {
          test: /\.(?:mp3|wav|ogg|mp4)$/i,
          type: 'asset/resource',
        },
        {
          test: /\.(woff(2)?|eot|ttf|otf)$/i,
          type: 'asset/resource',
        },
        {
          test: /\.module\.scss$/, // Для SCSS модулей
          use: [
            MiniCssExtractPlugin.loader, 
            {
              loader: 'css-loader',
              options: {
                modules: {
                  namedExport: false,
                  exportLocalsConvention: 'as-is',
                },
                sourceMap: true,
              },
            },
            'sass-loader', // Обработка SCSS файлов
          ],
          exclude: /node_modules/,
        },
        {
          test: /\.scss$/, // Для обычных SCSS файлов (не модулей)
          use: [
            MiniCssExtractPlugin.loader, 
            'css-loader',
            'sass-loader',
          ],
          exclude: /\.module\.scss$/, // Исключаем файлы модулей
        },
      ],
    },
    resolve: {
      extensions: ['.js', '.jsx', '.ts', '.tsx', '.css', '.scss'],
      alias: {
        '~': path.resolve(__dirname, './src/'),
      },
    },
    optimization: {
      splitChunks: {
        chunks: 'all',
      },
    },
    performance: {
      hints: false,
      maxEntrypointSize: 512000,
      maxAssetSize: 512000,
    },
    devServer: {
      port: env.port,
      hot: true,
      historyApiFallback: true,
      open: true,
    },
    watchOptions: {
      ignored: /node_modules/,
    },
    plugins: [
      new MiniCssExtractPlugin({
        filename: 'css/[name].[contenthash].css',
      }),
      new HtmlWebpackPlugin({
        favicon: './src/assets/svg/main_logo.svg',
        template: path.resolve(__dirname, './src/index.html'),
      }),
    ],
  };
};
